import tensorflow as tf
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense, Dropout, LSTM, Bidirectional, BatchNormalization, Activation, \
    LeakyReLU  # , CuDNNLSTM
from tensorflow.keras import Model, Input
# from tensorflow.keras.layers.advanced_activations import LeakyReLU
from tensorflow.keras.callbacks import ModelCheckpoint
from tensorboard.plugins.hparams import api as hp
import pickle, sys, os
import numpy as np
from datetime import datetime


def main():

    args = sys.argv[1:]
    time = datetime.now().strftime('%m-%d-%Y_%H:%M')
    create_test_model('logs/classifier_' + args[0] + '_Sequence_' + args[1] + '_Epochs_' + args[2], time )

def create_test_model(logdir, time):
    args = sys.argv[1:]
    pickle_in = open("pickles/classifier_" + args[0] + "_network_input_" + args[1]  +"_normalized.pickle", "rb")
    X = pickle.load(pickle_in)

    print(X[:3])
    pickle_in = open("pickles/classifier_" + args[0] + "_network_" + args[1] + "_labels.pickle", "rb")
    y = pickle.load(pickle_in)
    print(y[:500])

    pickle_in = open("pickles/classifier_" + args[0] + "_network_input_" + args[1]  +"_normalized_EVALUATION.pickle", "rb")
    X_test = pickle.load(pickle_in)

    print(X[:3])
    pickle_in = open("pickles/classifier_" + args[0] + "_network_" + args[1] +"_labels_EVALUATION.pickle", "rb")
    y_test = pickle.load(pickle_in)

    y = np.array(y)
    X = np.array(X)

    y_test = np.array(y_test)
    X_test = np.array(X_test)

    model = Sequential()


    model.add(LSTM(32, input_shape=(X.shape[1:]), return_sequences=True))
    model.add(Dropout(0.1))
    model.add(BatchNormalization())

    model.add(Dense(32))

    model.add(Bidirectional(LSTM(32)))
    model.add(Dropout(0.1))
    model.add(BatchNormalization())

    model.add(Dense(1, activation='sigmoid'))  # softmax

    # opt = tf.keras.optimizers.Adam(lr=0.002, decay=1e-6)

    # y = tf.keras.utils.to_categorical(y)

    # Compile model
    model.compile(
        loss='binary_crossentropy',  # categorical_crossentropy'
        optimizer='rmsprop',
        metrics=['accuracy'],
    )

    model.fit(X,
              y,
              epochs=int(args[2]),
              batch_size=128,

              )

    val_loss, val_acc = model.evaluate(X_test, y_test)
    print(val_loss)
    print(val_acc)
    if not os.path.exists(logdir):
        os.makedirs(logdir)
    model.save(logdir + "/" + str(val_acc) + "_trained_model_" +time + ".h5")
    return val_acc


if __name__ == '__main__':
    main()
