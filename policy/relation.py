import pandas as pd
import numpy as np
from py2neo import Graph, Node, Relationship
from pyhanlp import HanLP
import requests


def dependency_relation(sentence):
    relation = HanLP.parseDependency(sentence)
    return relation


if __name__ == "__main__":
    headers = {
        'User-Agent': 'Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Mobile Safari/537.36'
    }
    t = requests.get('https://www.fujiansme.com/index.php?m=content&c=index&a=show&catid=925&id=5035', headers=headers)
    fp = open('1.txt', 'w')
    fp.write(t.text)