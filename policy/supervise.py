import pandas as pd
import numpy as np
import policy_util
import re
from sklearn.model_selection import train_test_split

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
            d = {
                'sent': p,
                'entities': entity
            }
            result.append(d)
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


def train_policy(model, data_x, data_y):
    X_train, X_test, Y_train, Y_test = train_test_split(data_x, data_y, test_size=0.1, random_state=0)
    print(X_train)
    print()


if __name__ == '__main__':
    data = init_train_test_data('data/policy_fujian_content.txt') #初始化训练数据，从文章提取所有训练数据
    print("############成功提取所有数据###############")
    label = make_label(data)