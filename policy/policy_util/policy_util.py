import csv
import re
import json
import pandas as pd


DepartMap = {
    699: '福建省工业和信息化厅',
    700: '福建省财政厅',
    701: '福建省科技厅',
    702: '福建省商务厅',
    703: '福建省改革发展委员会',
    704: '福建省税务局',
    706: '福建省生态环境厅',
    707: '福建省质量技术监督局',
    708: '福建省自然资源厅',
    709: '福建省海洋与渔业厅',
    710: '福建省文旅局',
    711: '福建省林业局',
    712: '福建省水利厅',
    713: '福建省食品药品监督监管局',
    714: '福建省物价局',
    731: '福建省人力资源和社会保障厅',
    741: '福建省人民政府',
    925: '省级厅级政策'

}


def write_csv(data, path):
    with open(path, 'w', encoding='utf-8-sig') as fp:
        csv_writer = csv.writer(fp)
        if data:
            keys = data[0].keys()
        csv_writer.writerow(keys)
        for row in data:
            data_row = [row[key] for key in keys]
            csv_writer.writerow(data_row)
        fp.close()


def cut_sentence(paragraph):
    pattern = '《|》|。|[\s]|；'
    result = re.split(pattern, paragraph)
    return result


def find_all_special_entity(sent):
    index = 0
    res = []
    while sent.find('“') >= 0 and index < len(sent):
        start = sent.find('“', index)
        end = sent.find('”', index)
        if start >= 0 and end >= 0:
            res.append(sent[start + 1:end])
            index = end + 1
        else:
            break
    return res


def get_json_document(data_dict_list, path):
    write_list = []
    for data in data_dict_list:
        for key in data.keys():
            if isinstance(key, list):
                data[key] = ','.join(data[key])
        d = json.dumps(data, indent=4, separators=(',', ':'))
        write_list.append(d)
    with open(path, 'w', encoding='utf-8') as fp:
        fp.writelines(write_list)


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


def insert_policy_title(path, data: pd.DataFrame):
    result = []
    for index, row in data.iterrows():
        insert = 'INSERT INTO policy_title(title,depart,type,date,link) VALUES(\'#{title}\',\'#{department}\',{#type},\'{#date}\',\'{#link}\');'
        insert = insert.replace('#{title}', row['title'])
        insert = insert.replace('#{department}', DepartMap.get(int(row['depart_id']), '政府'))
        depart_id = int(row['depart_id'])
        if depart_id in [700, 701, 702]:
            type = 1
        elif depart_id == 699:
            type = 2
        elif depart_id in [703, 704, 713, 714, 731]:
            type = 3
        elif depart_id in [706, 708, 709, 711, 712]:
            type = 4
        elif depart_id == 710:
            type = 5
        else:
            type = 6
        insert = insert.replace('{#type}', str(type))
        insert = insert.replace('{#date}', row['date'])
        insert = insert.replace('{#link}', row['detail'])
        print(insert)
        result.append(insert)
    with open(path, 'w', encoding='utf-8') as fp:
        for r in result:
            fp.write(r)
            fp.write('\n')


