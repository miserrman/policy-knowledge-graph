import pandas as pd
import numpy as np
from py2neo import Graph, Node, Relationship
from pyhanlp import HanLP
import requests
import policy_util
import jieba.posseg as posseg
import gensim


def dependency_relation(sentence):
    relation = HanLP.parseDependency(sentence)
    return relation


def get_vectorization_init(data):
    sents = []
    for index, row in data.iterrows():
        content = row['content']
        paras = policy_util.cut_sentence(content)
        for para in paras:
            sent = []
            words = posseg.cut(para)
            for word in words:
                if word.flag != 'w':
                    sent.append(word.word)
            sents.append(sent)
    return sents


def word_vectorization(data: pd.DataFrame, emb_size=30):
    sents = get_vectorization_init(data)
    model = gensim.models.Word2Vec(sents, min_count=3, size=emb_size, window=5, iter=10)
    return model


def combine_entity(entities, para):
    res = [entities[0]]
    index = 0
    for i in range(1, len(entities)):
        if para.find(res[index] + entities[i]) >= 0:
            res[index] += entities[i]
        else:
            res.append(entities[i])
            index += 1
    if len(res) <= 1:
        return None
    else:
        return res



def get_entity_pair(sentence, entities):
    entity_dict = dict()
    for entity in entities:
        index = sentence.find(entity)
        entity_dict[entity] = index
    entity_dict = sorted(entity_dict.items(), key=lambda d: d[1])
    res = []
    index = 0
    i = 0
    for key, value in entity_dict:
        if i == 0:
            res.append(key)
            i += 1
            continue
        if res[index].find(key) >= 0 or key.find(res[index]) >= 0:
            if len(key) > len(res[index]):
                res[index] = key
        else:
            index += 1
            res.append(key)
        i += 1
    return res


def _init_policy_content(data):
    res = []
    for index, row in data.iterrows():
        entity_str = row['entity']
        entities = str(entity_str).split('|')
        entities = get_entity_pair(row['para'], entities)
        combine = combine_entity(entities, row['para'])
        if not combine:
            continue
        if len(combine) > 2:
            continue
        d = {
            'id': row['id'],
            'para': row['para'],
            'entity': '|'.join(combine),
            'label': -1
        }
        res.append(d)
    policy_util.write_csv(res, 'data/result/policy_manifest_label.csv')


if __name__ == "__main__":
    # policy_content = pd.read_csv("data/policy_fujian_content.csv", header=0)
    # word_vectorization(policy_content)
    data = pd.read_csv('./data/result/policy_fujian_content_entity1.csv', header=0)
    _init_policy_content(data)


