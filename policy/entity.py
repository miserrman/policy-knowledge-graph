from collections import Counter
import re
import pandas as pd
import jieba.posseg
import jieba.analyse
from apriori import *


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


if __name__ == '__main__':
    content = pd.read_csv('data/policy_content.csv', header=0)
    entity_recognition(content)


