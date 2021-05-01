import pandas as pd
data = pd.read_csv("data/nodes.csv", header=0)
for index, row in data.iterrows():
    data.iloc[index, -1] = data.iloc[index, 2]
    data.iloc[index, 2] = "GraphEntity"
    id = str(data.iloc[index, 3]).split(',')
    id = [str(int(i) + 1) for i in id]
    id = ','.join(id)
    data.iloc[index, 3] = id
data.to_csv("data/nodes_fake.csv", index=False)