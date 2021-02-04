import pandas as pd
import numpy as np
import policy_util
import re
from sklearn.model_selection import train_test_split
import gensim
import torch
from  torch import nn
from torch.utils.data import DataLoader, Dataset, TensorDataset


class RNN(nn.Module):
    def __init__(self, input_len, hidden_size, num_layers, output_len):
        super(RNN, self).__init__()
        self.lstm = nn.LSTM(input_size=input_len,
                            hidden_size=hidden_size,
                            num_layers=num_layers,
                            batch_first=True)
        self.output_layers = nn.Linear(in_features=hidden_size, out_features=output_len)

    def forward(self, x):
        lstm_out, (h_n, h_c) = self.lstm(x, None)
        output = self.output_layers(lstm_out[:, -1, :])
        return output


ENTITY_TYPE_MAP = {
    'None': 0,
    'Entity': 1,
    'Event': 2,
    'Location': 3,
    'Organization': 4,
    'Department': 5,
    'Person': 6
}

def init_train_test_data(path):
    """
    提取所有标注的数据，格式为字典 {para(原话):"", ([(实体，类型)])}
    :param path:
    :return:
    """
    pp = []
    paras = []
    result = []
    with open(path, 'r', encoding='utf-8') as fp:
        lines = fp.readlines()
        for line in lines:
            sents = policy_util.cut_sentence(line)
            paras += sents
        for para in paras:
            p = re.sub("[A-Za-z0-9\$\#\*\]\[]", "", para)
            entity = extract_mark_label(para)
            if re.search(r'“.*”', p):
                event = str(re.search(r'“.*”', p).group())
                entity.append((event[1:-1], "Event"))
            if not entity:
                continue
            if len(p) > 100 or len(entity) < 2:
                continue
            pp.append(p)
            d = {
                'sent': p,
                'entities': entity
            }
            result.append(d)
    print(len(set(pp)))
    return result


def extract_mark_label(sent: str):
    left = 0
    start = -1
    res = []
    words = []
    for i in range(len(sent)):
        if i < len(sent) and sent[i] == '[' and sent[i + 1] == '$':
            if left == 0:
                start = i
            left += 1
        elif i > 0 and sent[i] == ']' and sent[i - 1] == '*':
                left -= 1
        if left < 0:
            print('提取错误 %s' % sent)
            break
        elif left == 0 and start != -1:
            res.append(sent[start:i + 1])
            start = -1
    for r in res:
        word = re.sub("[A-Za-z0-9\$\#\*\]\[]", "", r)
        t = len(r) - 1
        while t >= 0 and r[t] != '#':
            t -= 1
        kind = r[t + 1: len(r) - 2]
        words.append((word, kind))
    return list(set(words))


def max_sent(dict_list):
    length = 0
    p = None
    for d in dict_list:
        if length < len(d['sent']):
            length = len(d['sent'])
            p = d['sent']
    return length, p


def make_label(dict_list):
    max_length, p = max_sent(dict_list)
    print(max_length, p)
    labels = np.zeros((len(dict_list), max_length))
    index = 0
    for d in dict_list:
        para = d['sent']
        entities = d['entities']
        for entity, e_type in entities:
            index_list = policy_util.find_all(para, entity)
            for l in index_list:
                for i in range(l, l + len(entity)):
                    labels[index][i] = ENTITY_TYPE_MAP[e_type]
        index += 1
    return labels


def word2vector(dict_list, train_data, emb_size=30):
    word2_train_list = []
    max_length, _ = max_sent(train_data)
    for d in train_data:
        text = d['sent']
        word2_train_list.append(list(text))
    model = gensim.models.Word2Vec(word2_train_list, size=emb_size, min_count=1, iter=10)
    word2_make_list = []
    for d in dict_list:
        text = d['sent']
        word2_make_list.append(list(text))
    word2_result = []
    for words in word2_make_list:
        r = []
        for char in words:
            vector_list = model.wv[char]
            r += list(vector_list)
        for i in range(len(r), max_length):
            r.append(0.0)
        word2_result.append(r)
    return word2_result


def train_policy(data_x, data_y, epoches, batch_size, model=None):
    X_train, X_test, Y_train, Y_test = train_test_split(data_x, data_y, test_size=0.1, random_state=0)
    X_train = word2vector(X_train, data_x)
    X_test = word2vector(X_test, data_x)
    train_dataset = TensorDataset(X_train, Y_train)
    test_dataset = TensorDataset(X_test, Y_test)
    train_loader = DataLoader(dataset=train_dataset, batch_size=batch_size, shuffle=True, num_workers=2)
    #定义损失函数和优化器
    a
    for i in range(epoches):
        for step, (batch_x, batch_y) in enumerate(train_loader):


if __name__ == '__main__':
    data = init_train_test_data('data/policy_fujian_content.txt') #初始化训练数据，从文章提取所有训练数据
    # print("############成功提取所有数据###############")
    label = make_label(data)
    train_policy(data, label)


