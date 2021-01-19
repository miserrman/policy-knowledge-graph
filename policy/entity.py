from collections import Counter
import re
import pandas as pd
import jieba.posseg
import jieba.analyse
from apriori import *
from pyhanlp import HanLP
import regex
import policy_util
from sklearn import feature_extraction
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.feature_extraction.text import CountVectorizer


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
    """
    从一个句子数组里提取名词词语
    :param paragraphes:
    :return:
    """
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
    extract_term = [str(term) for term in extract_term]
    return extract_term


def tf_idf_statistic(data):
    """
    使用TF-IDF判断文章的词频
    :param data:
    :return:
    """
    all_word = []
    for index, row in data.iterrows():
        content = row['content']
        paras = cut_sentence(content)
        words = extract_entity(paras)
        word = ' '.join(words)
        all_word.append(word)
    vectorizer = CountVectorizer()
    transformer = TfidfTransformer()
    tfidf = transformer.fit_transform(vectorizer.fit_transform(all_word))
    word = vectorizer.get_feature_names()
    weight = tfidf.toarray()
    return word, weight


def entity_recognition(data: pd.DataFrame):
    paragraphes = []
    for index, row in data.iterrows():
        para = str(row['content'])
        paragraphes.append(para)
    term_data = extract_entity(paragraphes)
    m, n = apriori(term_data, minSupport=0.001)
    rules = generateRules(m, n, minConf=0.01)
    print(rules)


def extract_high_rate_word(data: pd.DataFrame, rate):
    """
    提取高频词作为实体，使用tf-idf
    :param data: 使用数据
    :param rate: 提取率，提取词频前%n的词
    :return:
    """
    word, weight = tf_idf_statistic(data)
    result = []
    for i in range(len(weight)):
        res = []
        content = dict()
        for j in range(len(word)):
            if weight[i][j] > 0:
                content[word[j]] = weight[i][j]
        length = len(content)
        content = sorted(content.items(), key=lambda d: d[1], reverse=True)
        t = 0
        for key, value in content:
            if t >= int(length * rate):
                break
            res.append(key)
            t += 1
        result.append(res)
    entity_paras = []
    entity_includes = []
    for index, row in data.iterrows():
        content = row['content']
        res, include_entity = get_entity_para(content, result[index])
        if index == 2:
            print(res)
            print(include_entity)
        entity_paras.append(res)
        entity_includes.append(include_entity)
    policy_entity_res = []
    for i in range(len(entity_paras)):
        l = len(entity_paras[i])
        for j in range(l):
            entity_res = {
                'id': i,
                'para': entity_paras[i][j],
                'entity': '|'.join(entity_includes[i][j])
            }
            policy_entity_res.append(entity_res)
    policy_util.write_csv(policy_entity_res, 'data/result/policy_fujian_content_entity.csv')


def get_entity_para(content, entity):
    if not content:
        return []
    paras = cut_sentence(content)
    result = []
    include_entity = []
    for para in paras:
        entities = []
        count = 0
        for e in entity:
            if para.find(e) >= 0:
                entities.append(e)
                count += 1
        if count >= 2:
            result.append(para)
            include_entity.append(entities)
    return result, include_entity


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
    # entity_recognition(content)#使用关联分析方法分析
    # data = pd.read_csv('data/policy_fujian.csv', header=0)
    # extract_special_policy(data)#使用依存关系分析标题
    data = pd.read_csv('data/policy_fujian_content.csv', header=0)
    extract_high_rate_word(data, 0.1)

