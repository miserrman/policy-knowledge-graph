import pandas as pd
import numpy as np
from py2neo import Graph, Node, Relationship


connection = Graph('http://localhost:7474', username='neo4j', password='123456')
graph = connection.begin()
node1 = Node('label', name='maiduojian', primary_key='name')
node2 = Node('label', name='god', primary_key='name')
relationship = Relationship(node1, 'is', node2)
graph.create(node1)
graph.create(node2)
graph.create(relationship)
print(graph)
graph.commit()