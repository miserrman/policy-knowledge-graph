import gensim
import numpy as np
from gensim.models import Word2Vec
from sklearn import preprocessing
import tensorflow as tf
import processing
from model.model import Model
EN_MAP = {
    'Ent': 'Entity',
    'Eve': 'Event',
    'Loc': 'Location',
    'Org': 'Organization',
    'Dep': 'Department',
    'Per': 'Person'
}
class OneHot(object):
    def __init__(self):
        self.le = preprocessing.LabelEncoder()

    def encode(self, target_list):
        integer_encoded = self.le.fit_transform(np.array(target_list))
        # print(self.le.classes_,len(self.le.classes_))
        return integer_encoded, self.le.classes_

    def decode(self, encoder_list):
        # print(self.le.classes_,len(self.le.classes_))
        return self.le.inverse_transform(np.argmax(np.array(encoder_list), axis=1))

    def get_classes(self):
        return self.le.classes_

def transform_to_BIO(data):
    '''
    生成每个字对应的BIO类型
    :param data:
    :return: 二维数组，生成单个字标注（BIOES）
    '''
    BIO=[]
    for line in data:
        persent = line['sent']
        entities = line['entities']
        persent_BIO = [[w, 'O'] for w in persent]
        for entity, e_type in entities:
            index_list = processing.find_all(persent, entity)
            for l in index_list:
                if len(entity) == 1:
                    persent_BIO[l][1] = 'S-' + e_type[0:3]
                elif len(entity) == 2:
                    persent_BIO[l][1]  = 'B-' + e_type[0:3]
                    persent_BIO[l + 1][1] = 'E-' + e_type[0:3]
                else:
                    persent_BIO[l][1]  = 'B-' + e_type[0:3]
                    for i in range(l + 1, l + len(entity) - 1):
                        persent_BIO[i][1]  = 'I-' + e_type[0:3]
                    persent_BIO[i + 1][1]  = 'E-' + e_type[0:3]
        BIO.append(persent_BIO)

    source_string = []
    target_string = []
    for line in BIO:
        s = []
        t = []
        for w in line:
            s.append(w[0])
            t.append(w[1])
        source_string.append(s)
        target_string.append(t)
    return source_string, target_string

def get_vec_model(vec_path):
    vec_model = gensim.models.Word2Vec.load(vec_path)
    return vec_model

def get_target_label(target_string, max_sequence=100):
    '''
    使每个句子的标签长度一致，并进行编码
    :param target_string:
    :param max_sequence:
    :return: 特征编码结果，特征编码模型，
    '''
    onehot_model = OneHot()
    for i in range(0, len(target_string)):
        if len(target_string[i]) < max_sequence:
            #target_string[i] = target_string[i].extend(["O"]*(max_sequence - len(target_string[i])))
            target_string[i].extend(["O"] * (max_sequence - len(target_string[i])))
            if target_string[i] is None:
                target_string[i] = ["O"]*max_sequence
        else:
            if target_string[i] is None:
                target_string[i] = ["O"]*max_sequence
            else:
                target_string[i] = target_string[i][0:max_sequence]
    num_rows = len(target_string)
    flat_list = [item for sublist in target_string for item in sublist]#所有O变成一列
    target_vector , classes= onehot_model.encode(flat_list)
    target_vector = target_vector.reshape(-1, max_sequence)
    return target_vector, onehot_model

def get_train_feature(source_string, vec_model, max_sequence=100):
    index2word_set = set(vec_model.wv.index2word)
    row_vector_list = []
    for source_line in source_string:
        i = 0
        row_vector = []
        for source_word in source_line:
            if i < max_sequence:
                if source_word in index2word_set:
                    row_vector= np.append(row_vector, vec_model.wv[source_word])
                else:
                    row_vector = np.append(row_vector, np.zeros(vec_model.trainables.layer1_size, dtype='float32'))
            else:
                break
            i += 1
        if len(source_line) < max_sequence:
            row_vector = np.append(row_vector,
                                   np.zeros((vec_model.trainables.layer1_size * (max_sequence - len(source_line)),),dtype='float32'))
        row_vector_list.append(row_vector)
    return np.matrix(row_vector_list)

def predict_lstm_crf(model, predcit_feature, model_path):
    embedding_size = model.nc.embedding_size
    unit_num = model.nc.unit_num
    output_size = model.nc.output_size
    batch_size = model.nc.batch_size
    seq_length = model.nc.seq_length #与给定句子最长参数一致
    lr = model.nc.lr

    X = tf.placeholder(tf.float32, shape=[batch_size, seq_length * embedding_size])
    pred = model.lstm_crf(X)
    saver = tf.train.Saver(tf.global_variables())
    predict_label = []
    with tf.Session() as sess:
        module_file = tf.train.latest_checkpoint(model_path)
        saver.restore(sess, module_file)
        for step in range(len(predcit_feature) - 1):
            prob = sess.run(pred, feed_dict={X: predcit_feature[step]})
            predict = prob.reshape((-1)).reshape(-1, output_size)
            predict_label.append(predict)
    return predict_label

def get_sentnums(article):
    sent = processing.cut_sentence(article)
    return len(sent)

def get_predictEntity(X_test, predict_Y, article_start, articlenums):
    predict_num = 0
    predict_label = ''
    entity_loc = []
    preb = 0
    anum = 0
    total = articlenums[0]
    sentnum=0
    for i in range(len(predict_Y)):#所有句子
        for j in range(len(predict_Y[i])):
            if j >= len(X_test[i]):
                break
            predict_line = predict_Y[i]#每句话
            if predict_line[j][:1] == 'B':
                preb = j
                type = predict_line[j][2:]
            elif predict_line[j][:1] == 'E':
                if predict_line[preb][:1] != 'B':
                    continue
                if predict_line[j][2:] != type:
                    continue
                for k in range(preb, j):
                    if k == preb:
                        predict_label = predict_line[k]
                        continue
                    predict_label += predict_line[k]
                if k == j - 1:
                    predict_num += 1
                    predict_label += predict_line[j]
                    entity = ''
                    for l in range(preb, j):
                        entity += X_test[i][l]
                    entity += X_test[i][j]
                    entity_loc.append([article_start+anum, sentnum,entity, EN_MAP[type]])
                    # print(loc)
            elif predict_line[j] == 'S':
                predict_num += 1
                entity = X_test[i][j]
                entity_loc.append([article_start+anum, sentnum, entity, EN_MAP[type]])
                # print(loc)
        # entity_loc.append(loc)
        sentnum += 1
        if i == total-1:
            anum += 1
            total += articlenums[anum]
            sentnum = 0

    # print(entity_loc)
    print('识别出的实体个数: ', predict_num)
    return entity_loc

def predict(X_test, vec_model, onehot_model, model_rpath):
    tf.reset_default_graph()
    model = Model()
    feature = get_train_feature(X_test, vec_model, max_sequence=100)
    predict_result = predict_lstm_crf(model, feature, model_rpath)

    predict_Y = []
    for line in predict_result:
        predict_label = onehot_model.decode(line)
        predict_Y.append(predict_label)
        # print(predict_label)
    return predict_Y


def AnalysisContent(model_rpath, vec_path, title, content, contentId):
    data = processing.init_train_test_data('model/policy_fujian_content.txt')
    source_string, target_string = transform_to_BIO(data)
    vec_model = get_vec_model(vec_path)
    target_vector, onehot_model = get_target_label(target_string, max_sequence=100)

    sent = processing.cut_sentence(content)
    if len(sent) == 1:
        X_test = [sent[0],sent[0]]
    else:
        X_test = sent
    predict_content = predict(X_test, vec_model, onehot_model, model_rpath)
    contentEntity = get_predictEntity(X_test, predict_content,contentId,[len(X_test)])

    X_test = [title,title]
    predict_title = predict(X_test, vec_model, onehot_model, model_rpath)
    titleEntity = get_predictEntity(X_test, predict_title,contentId,[len(X_test)])

    return contentEntity, titleEntity

