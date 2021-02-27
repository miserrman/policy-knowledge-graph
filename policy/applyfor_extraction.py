import jieba.posseg as psg
import csv
from pyhanlp import *
import operator
import jieba.analyse
import re

FQWcontent_location="data/policy_fujian_content.csv"
FQWtitle_location="data/policy_fujian.csv"
LYtitle_location="data/policy_id.csv"
LYcontent_location="data/policy_content.csv"
FQWApplyFor="data/policy_applyfor.csv"
w_location="data/result/policy_ApplyFor.csv"
def get_content(location,contents):
     with open(location, 'r', encoding='utf-8') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            contents.append(str(row['content']))
        csvfile.close()
     return contents

def cut_sentence(paragraph):
    pattern = '。|，|;|；|\?|[\s]|且'
    result = re.split(pattern, paragraph)
    # tem=result
    # for sen in tem:
    #     if len(sen)>60:
    #         result.remove(sen)
    #         sen=re.split('，', sen)
    #         result=result+sen
    for i in range(result.count('')):
        result.remove('')
    return result

def processingApplyFor(contents):
    with open(w_location, 'w', encoding='utf-8') as csvfile:
        w=csv.writer(csvfile)
        w.writerow(['sentence','entity','compare','num'])
        pattern1='[一二三四五六七八九零十]、'
        i=0
        j=0
        for content in contents:
            #restrain=['申报条件','申报基本要求','申报主体','申报要求','申报领域','申报对象','条件']
            restrain = ['条件', '目标','名额', '范围','补助标准','申报要求', '申报基本要求', '主体', '领域', '对象']
            paras = re.split(pattern1, content)#每段
            for para in paras:
                for r in restrain:
                    if para.find(r+'\n')>=0 or para.find(r+'：\n')>=0:#找到该段有条件要提取
                        para=re.sub(r'[\n]*[（(][一二三四五六七八九十]+[）)]', '', para)
                        para = re.sub(r'[\n]*[123456789]+\.', '', para)
                        for persentence in cut_sentence(para):
                            entity,compare,data= Dependency_Parsing(persentence)
                            w.writerow([str(i)+'#'+persentence,"、".join(str(j) for j in entity),"、".join(str(j) for j in compare),"、".join(str(j) for j in data)])
                            print('\n'+str(i)+'#'+persentence,entity,compare,data)
            i=i+1
    csvfile.close()

def Dependency_Parsing(persentence):
    entity = []
    compare = []
    data = []
    tem = 0
    id = 0
    compare_flag = ['不少于', '少于', '不低于', '低于', '不高于', '高于', '不多于', '多于','不超过','超过']
    analysis = HanLP.parseDependency(persentence)
    word_array = analysis.getWordArray()
    #print(analysis)
    for i in range(len(word_array)):
        item = word_array[i]
        #print("%s        %s        (%s)        %s" % (item.LEMMA, item.CPOSTAG, item.DEPREL, item.HEAD.LEMMA))
        if (item.DEPREL == '定中关系' and item.HEAD.CPOSTAG.find('n') >= 0):
            if ((i - 1 >= 0) and word_array[i - 1].DEPREL == '定中关系'):
                continue
            e = ''
            for num in range(0, item.HEAD.ID - item.ID):
                e = e + word_array[i + num].LEMMA
            e = '(' + e + ')' + item.HEAD.LEMMA
            entity.append(e)
            id = item.HEAD.ID

        # 包含比较关系以及数字
        for per in compare_flag:
            if (i + 2 < len(word_array) and (word_array[i + 1].LEMMA + word_array[i + 2].LEMMA) == per):
                compare.append(per)
                if (id < item.ID - 1):
                    entity.append('(' + word_array[i - 1].LEMMA + ')' + item.LEMMA)
                elif (id < item.ID):
                    entity.append(item.LEMMA)
                id = item.ID
                break
            elif (i + 1 < len(word_array) and word_array[i + 1].LEMMA == per and item.LEMMA != '不'):
                compare.append(per)
                if (id < word_array[i].ID - 1):
                    entity.append('(' + word_array[i - 1].LEMMA + ')' + item.LEMMA)
                elif (id < item.ID):
                    entity.append(item.LEMMA)
                id = item.ID
                break

        # 提取名词及定语
        if (item.CPOSTAG.find('n') >= 0 and item.ID > id):
            entity.append(item.LEMMA)
            id = word_array[i].ID

        # 数词
        if (item.CPOSTAG == 'm'):
            if (item.DEPREL == "定中关系"):
                data.append(persentence[persentence.find(item.LEMMA):persentence.find(
                    item.HEAD.LEMMA, tem) + len(item.HEAD.LEMMA)])
                tem = persentence.find(item.HEAD.LEMMA) +  len(item.HEAD.LEMMA)
            else:
                data.append(item.LEMMA)
    return entity,compare,data


if __name__=='__main__':
    contents=[]
    contents=get_content(FQWApplyFor,contents)
    processingApplyFor(contents)
