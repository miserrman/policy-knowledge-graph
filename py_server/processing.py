import re

def cut_sentence(paragraph):
    pattern = '。|[\s]|；|　'
    result = re.split(pattern, paragraph)
    result = [result[j] for j in range(len(result)) if result[j] != '']
    return result

def init_train_test_data(path):
    """
    提取所有标注的数据，格式为字典 {para(原话):"", ([(实体，类型)])}
    :param path:
    :return:
    """
    pp = []
    paras = []
    result = []
    sentnums = []
    with open(path, 'r', encoding='utf-8-sig') as fp:
        lines = fp.readlines()
        for line in lines:
            sents = cut_sentence(line)
            sentnums.append(sents)
            for per in sents:
                if per != '':
                    paras.append(per)

        for para in paras:
            p = re.sub("[A-Za-z0-9\$\#\*\]\[]", "", para)
            entity = extract_mark_label(para)
            # if re.search(r'“.*”', p):
            #     event = str(re.search(r'“.*”', p).group())
            #     entity.append((event[1:-1], "Event"))
            if not entity:
                continue
            if len(p) > 100 or len(entity) < 2:
                continue
            pp.append(p)
            d = {
                'sent': p,
                'entities': entity,
                'original_sent': para,
            }
            result.append(d)
    # print(len(set(pp)))
    return result

def extract_mark_label(sent: str):
    left = 0
    start = -1
    res = []
    words = []
    index = []
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

    return words#list(set(words))


def find_all(s: str, item):
    index = 0
    res = []
    if len(item) == 0:
        return []
    while index < len(s):
        t = s.find(item, index)
        if t < 0:
            break
        else:
            res.append(t)
            index = t + len(item)
    return res

def dealwithTitle(title):
    '''
    title_head存放组织名
    title_back存放除组织名以外的部分
    title_key存放《》“”提取的内容
    :param title:
    :return:
    '''
    title_head = []
    title_back = ''
    title_key = []

    # 提取所有《》
    index = title.find('《')
    while index != -1:
        title_key.append(title[index + 1:title.find('》', index + 1)])
        index = title.find('《', index + 1)
    # 提取所有“”
    index = title.find('“')
    while index != -1:
        title_key.append(title[index + 1:title.find('”', index + 1)])
        index = title.find('“', index + 1)

    # 根据(关于和：),提取组织名
    index1 = title.find('关于')
    index2 = title.find('：')
    if index1 >= 0 and title.find('《') != index1 - 1:
        title_head = title[:index1]
        title_head = re.sub(r'[：]', '', title_head)
        if title_head.split(' '):
            title_head = title_head.split(' ')
        #除去的通知、的公示等
        title_back = title[index1 + 2:]
        title_back = re.sub(r' ', '', title_back)
        if title_back.find('的', len(title_back) - 5):
            title_back = title_back[:title_back.find('的', len(title_back) - 5)]
    else:
        if index2 >= 0:
            title_head = title[:index2]
            if title_head.split(' '):
                title_head = title_head.split(' ')
            # 除去的通知、的公示等
            title_back = title[index2 + 1:]
            title_back = re.sub(r' ', '', title_back)
            if title_back.find('的', len(title_back) - 5) < 0:
                title_back = title_back[:title_back.find('的', len(title_back) - 5)]
    if title_back == '':
        title_back = title
    print('王扒皮٩(๑`^´๑)۶略略略')
    return title_head, title_back, title_key