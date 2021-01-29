import csv
import re


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
    pattern = '《|》|。|\.|[\s]'
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
