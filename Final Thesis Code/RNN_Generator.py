import tensorflow as tf
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense, Dropout, LSTM, Bidirectional, BatchNormalization, Activation, LeakyReLU#, CuDNNLSTM
from tensorflow.keras import Model, Input
#from tensorflow.keras.layers.advanced_activations import LeakyReLU
from tensorflow.keras.callbacks import ModelCheckpoint
from tensorboard.plugins.hparams import api as hp
import pickle, sys, os
import numpy as np
from datetime import datetime
import time
def main():
    args = sys.argv[1:]
    time = datetime.now().strftime('%m-%d-%Y_%H:%M')

    create_test_model('logs/cutoff_' + args[3] + '/generator_' + args[4] + '_'+ args[1] +  '_Sequence_' + args[0] +'_' +args[2], time )

        
def create_test_model(logdir, date):
    args = sys.argv[1:]
    pickle_in = open("pickles/" + args[2] +"_network_input_" + args[1] + "_" + args[0] + "_" + args[3] + "_normalized.pickle","rb")
    X = pickle.load(pickle_in)

    pickle_in = open("pickles/" + args[2] +"_network_" + args[1] + "_" + args[0] + "_" + args[3] + "_labels.pickle","rb")
    y = pickle.load(pickle_in)

    pickle_in = open("pickles/" + args[2] + "_types_"+ args[1]  + "_" + args[0] + "_" + args[3] + ".pickle","rb")
    classes = pickle.load(pickle_in)
    num_classes = len(classes)
    y = np.array(y)
    X = np.array(X)

    start = time.time()
    model = Sequential()


    #OLD 32 nodes and 4-5 LSTM layers

    model.add(LSTM(int(args[4]), input_shape=(X.shape[1:]), return_sequences=True))
    model.add(Dropout(0.1))
    model.add(BatchNormalization())

    model.add(Dense(int(args[4])))

    model.add(Bidirectional(LSTM(int(args[4]))))
    model.add(Dropout(0.1))
    model.add(BatchNormalization())

    model.add(Dense(int(args[4])))

    model.add(Dense(num_classes, activation='softmax'))

    #opt = tf.keras.optimizers.Adam(lr=0.002, decay=1e-6)


    #y = tf.keras.utils.to_categorical(y)

    # Compile model
    model.compile(
        loss='sparse_categorical_crossentropy',  #categorical_crossentropy'
        optimizer='rmsprop',
        metrics=['accuracy'],
    )
        
    

    model.fit(X,
              y,
              epochs=int(args[5]),
              batch_size=128,

          )
    _, accuracy = model.evaluate(X,y)
    end = time.time()
    if not os.path.exists(logdir):
        os.makedirs(logdir)
    model.save(logdir + "/" + str(accuracy) + "_Epochs_" + args[5] + "_trained_model_elasped_time_" + str(int(end - start)) + ".h5")

    return accuracy
    


if __name__ == '__main__':
    main()
