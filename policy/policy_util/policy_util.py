import csv


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


