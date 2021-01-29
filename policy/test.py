import jieba.posseg
from pyhanlp import HanLP
result = HanLP.parseDependency("继续安排普惠金融发展资金，落实融资担保资金政策，促进“小微农”融资")
print(result)