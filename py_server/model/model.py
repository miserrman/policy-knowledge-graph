#coding:utf8
import tensorflow as tf
class NetConfig():
    def __init__(self):
        self.unit_num = 128  # 隐含层单元
        self.embedding_size = 128
        self.dropout_rate = None
        # self.output_size = 37
        self.output_size = 25
        self.lr = 0.001  # Learning Rate
        self.seq_length = 100
        self.batch_size = 1

class Model():
    def __init__(self):
        self.sess = tf.Session()
        self.nc = NetConfig()

    def lstm_crf(self, X):
        embedding_size = self.nc.embedding_size
        unit_num = self.nc.unit_num
        #dropout_rate = self.nc.dropout_rate
        output_size = self.nc.output_size
        batch_size = self.nc.batch_size
        seq_length = self.nc.seq_length
        lr = 0.001
        sess = self.sess

        cell_forward = tf.nn.rnn_cell.BasicLSTMCell(unit_num)
        cell_backward = tf.nn.rnn_cell.BasicLSTMCell(unit_num)
        input_bi_lstm = tf.reshape(X, [batch_size, seq_length, embedding_size])
        bi_outputs, bi_state = tf.nn.bidirectional_dynamic_rnn(cell_forward,
                                                               cell_backward, input_bi_lstm, dtype=tf.float32)

        bi_output = tf.concat(bi_outputs, axis=2)  # 256维是为什么？ 256*seq_length

        W = tf.get_variable("projection_w", [2 * unit_num, output_size])  # 新建权重矩阵
        b = tf.get_variable("projection_b", [output_size])
        x_reshape = tf.reshape(bi_output, [-1, 2 * unit_num])
        projection = tf.matmul(x_reshape, W) + b
        outputs = tf.reshape(projection, [batch_size, seq_length, output_size])
        return outputs
