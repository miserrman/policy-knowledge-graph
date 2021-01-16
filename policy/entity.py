from collections import Counter
import re
import pandas as pd
import jieba.posseg
import jieba.analyse
from apriori import *
from pyhanlp import HanLP
import regex
import policy_util


def cut_sentence(paragraph):
    pattern = '《|》|。|\.|[\s]'
    result = re.split(pattern, paragraph)
    return result


def build_vocabulary(data: pd.DataFrame):
    counter = Counter()
    vocabulary = dict()
    for index, row in data.iterrows():
        para = row['content']
        sentences = cut_sentence(para)
        for sent in sentences:
            for char in sent:
                counter[char] += 1
    num_most_common = len(counter)
    for char, _ in counter.most_common(num_most_common):
        vocabulary[char] = vocabulary.get(char, len(vocabulary))
    return vocabulary


def extract_entity(paragraphes):
    extract_term = []
    for para in paragraphes:
        sentences = cut_sentence(para)
        for sent in sentences:
            print(sent)
            term = []
            sent_seged = jieba.posseg.cut(sent.strip())
            for s in sent_seged:
                sege = str(s.flag)
                if sege.find('n') >= 0 and sege.find('v') < 0:
                    term.append(s.word)
            extract_term.append(term)
    return extract_term


def entity_recognition(data: pd.DataFrame):
    paragraphes = []
    for index, row in data.iterrows():
        para = str(row['content'])
        paragraphes.append(para)
    term_data = extract_entity(paragraphes)
    m, n = apriori(term_data, minSupport=0.001)
    rules = generateRules(m, n, minConf=0.01)
    # c = createC1(term_data)
    print(rules)


def extract_special_policy(data: pd.DataFrame):
    policy_result = []
    front_strip = ['关于', '印发']
    rear_strip = ['的公示', '的通知', '的实施意见', '的指导意见', '的通告', '的若干意见', '实施方案']
    for index, row in data.iterrows():
        title = row['title']
        title = str(title).split('：')[-1]
        if title.find("《") >= 0:
            title = str(re.search(r'《.+》', title).group())
            title = title[1:-1]
        else:
            for s in front_strip:
                t = title.find(s)
                if t >= 0:
                    title = title[t + len(s):]
            for r in rear_strip:
                t = title.find(r)
                if t >= 0:
                    title = title[:t]
        title = re.sub('（.+）', '', title)
        res = dependency_analysis(title)
        policy_result.append(res)
    policy_util.write_csv(policy_result, 'data/result/policy_entity.csv')


def dependency_analysis(sent):
    result = HanLP.parseDependency(sent)
    ROOT, SUBJECT, PREDICATE = '核心关系', '主谓关系', '宾'
    res = dict()
    key = ['root', 'sub', 'pre', 'sub_adj', 'pre_adj', 'entity']
    for word in result.iterator():
        type = str(word.DEPREL)
        if type.find(ROOT) >= 0:
            res['root'] = word.LEMMA
        elif type.find(SUBJECT) >= 0:
            res['sub'] = word.LEMMA
        elif type.find(PREDICATE) >= 0:
            res['pre'] = word.LEMMA
    res['entity'] = []
    for word in result.iterator():
        if str(word.CPOSTAG).find('n') >= 0 and str(word.CPOSTAG).find('v') < 0:
            res['entity'].append(word.LEMMA)
        if res.get('sub') and str(word.HEAD.LEMMA) == str(res['sub']):
            res['sub_adj'] = res['sub_adj'] + [word.LEMMA] if res.get('sub_adj') else [word.LEMMA]
        else:
            res['pre_adj'] = res['pre_adj'] + [word.LEMMA] if res.get('pre_adj') else [word.LEMMA]
    for k in key:
        res[k] = res.get(k, '空')
        if isinstance(res[k], list):
            res[k] = '|'.join(res[k])
    print(res)
    return res



if __name__ == '__main__':
    # content = pd.read_csv('data/policy_content.csv', header=0)
    # entity_recognition(content)
    data = pd.read_csv('data/policy_fujian.csv', header=0)
    extract_special_policy(data)

