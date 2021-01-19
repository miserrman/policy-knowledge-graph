import pandas as pd
import numpy as np
from py2neo import Graph, Node, Relationship
from pyhanlp import HanLP
import requests


def dependency_relation(sentence):
    relation = HanLP.parseDependency(sentence)
    return relation


if __name__ == "__main__":
    # headers = {
    #     'User-Agent': 'Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Mobile Safari/537.36'
    # }
    # t = requests.get('https://www.fujiansme.com/index.php?m=content&c=index&a=show&catid=700&id=2630', headers=headers)
    # fp = open('3.txt', 'w')
    # fp.write(t.text)
    d = {
        'a': 4,
        'b': 7,
        'c': 2
    }
    d = sorted(d.items(), key=lambda d:d[1], reverse=True)
    for key, value in d:
        print(value)
    print(d)