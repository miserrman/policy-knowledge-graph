import pandas as pd
import numpy as np
import policy_util
import re


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




if __name__ == '__main__':
    init_train_test_data('data/policy_fujian_content.txt')