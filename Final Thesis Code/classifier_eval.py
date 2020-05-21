import tensorflow as tf
import pickle, os, sys
from datetime import datetime
import numpy as np
from midiutil import MIDIFile
from _collections import defaultdict
import random
from tensorflow.keras.utils import to_categorical
import random
def main():
    args = sys.argv[1:]
    choice = input("sep or com or pitch?")
    if choice == "com":

        pickle_in = open("pickles/classifier_" + args[0] + "_types_" + args[2] + "_.pickle","rb")
        classifier_pitch_types = pickle.load(pickle_in)
        print(classifier_pitch_types)

        pickle_in = open("pickles/" + args[0] + "_types_"+ args[1]  + "_" + args[2] + "_" + args[3] + ".pickle","rb")
        pitch_types = pickle.load(pickle_in)

        print(classifier_pitch_types)
        print(pitch_types)

        pickle_in = open("pickles/" + args[1] + "_" + args[0] +"_Sequence_" + args[2] + "_CLASSIFICATION.pickle", "rb")
        X = pickle.load(pickle_in)

        '''
        pickle_in = open("classifier_" + args[0] + "_network_input_" + args[1] + "_" + args[2] + "_normalized.pickle", "rb")
        X = pickle.load(pickle_in)
    
        print(X[:3])
        pickle_in = open("classifier_" + args[0] + "_network_" + args[1] + "_" + args[2] + "_labels.pickle", "rb")
        y = pickle.load(pickle_in)
        print(y[:500])
    
        pickle_in = open(
            "classifier_" + args[0] + "_network_input_" + args[1] + "_" + args[2] + "_normalized_EVALUATION.pickle", "rb")
        X_test = pickle.load(pickle_in)
    
        print(X[:3])
        pickle_in = open("classifier_" + args[0] + "_network_" + args[1] + "_" + args[2] + "_labels_EVALUATION.pickle",
                         "rb")
        y_test = pickle.load(pickle_in)
        '''
        X = np.array(X)



        classifier_model = tf.keras.models.load_model('logs/classifier_note_Sequence_100_Epochs_2/0.9038657_trained_model_04-17-2020_01:19.h5')

        stop = 0
        average_loss = 0
        for sample in X:

            normalized = []
            for s in sample:
                normalized.append(classifier_pitch_types.index(pitch_types[s])/(len(classifier_pitch_types)-1))
            normalized = np.reshape(normalized, (1, 100, 1))
            prediction = classifier_model.predict(normalized)
            if args[1] == "chinese":
                average_loss += (1.0- prediction[0][0])
            else:
                average_loss += prediction[0][0]
            print(prediction)
       
            stop += 1
            if stop > 500:
                break
        print(average_loss/len(X))

    elif choice == "sep":
        pickle_in = open("pickles/classifier_note_types_" + args[1] + "_.pickle", "rb")
        classifier_pitch_types = pickle.load(pickle_in)
        print(classifier_pitch_types)

        pickle_in = open("pickles/pitch_types_" + args[0] + "_" + args[1] + "_" + args[2] + ".pickle", "rb")
        pitch_types = pickle.load(pickle_in)

        print(classifier_pitch_types)
        print(pitch_types)

        pickle_in = open("pickles/" + args[0] + "_pitch_Sequence_" + args[1] + "_CLASSIFICATION.pickle", "rb")
        X = pickle.load(pickle_in)



        pickle_in = open("pickles/duration_types_" + args[0] + "_" + args[1] + "_" + args[2] + ".pickle", "rb")
        duration_types = pickle.load(pickle_in)


        pickle_in = open("pickles/" + args[0] + "_duration_Sequence_" + args[1] + "_CLASSIFICATION.pickle", "rb")
        durations_classifications = pickle.load(pickle_in)

        classifier_model = tf.keras.models.load_model('logs/classifier_note_Sequence_100_Epochs_2/0.9038657_trained_model_04-17-2020_01:19.h5')
        combined_notes = []
        average_loss = 0
        for pitches, durations in zip(X, durations_classifications):
            temp_song = []
            for pitch, duration in zip(pitches, durations):
                temp_song.append(classifier_pitch_types.index(pitch_types[pitch] + "_" + duration_types[duration]) / (len(classifier_pitch_types) - 1))
            temp_song = np.reshape(temp_song, (1, 100, 1))
            prediction = classifier_model.predict(temp_song)
            if args[0] == "chinese":
                average_loss += (1.0 - prediction[0][0])
            else:
                average_loss += prediction[0][0]
            print(prediction)
        print(average_loss / len(X))

    elif choice == "pitch":
        pickle_in = open("pickles/classifier_" + args[0] + "_types_" + args[2] + ".pickle", "rb")
        classifier_pitch_types = pickle.load(pickle_in)
        print(classifier_pitch_types)

        pickle_in = open("pickles/" + args[0] + "_types_" + args[1] + "_" + args[2] + "_" + args[3] + ".pickle", "rb")
        pitch_types = pickle.load(pickle_in)

        print(classifier_pitch_types)
        print(pitch_types)

        pickle_in = open("pickles/" + args[1] + "_" + args[0] + "_Sequence_" + args[2] + "_CLASSIFICATION.pickle", "rb")
        X = pickle.load(pickle_in)

        classifier_model = tf.keras.models.load_model('logs/classifier_pitch_Sequence_100_Epochs_2/0.94601697_trained_model_04-17-2020_07:56.h5')

        stop = 0
        for sample in X:

            normalized = []
            for s in sample:
                normalized.append(classifier_pitch_types.index(pitch_types[s]) / (len(classifier_pitch_types) - 1))
            normalized = np.reshape(normalized, (1, 100, 1))
            print(classifier_model.predict(normalized))
            print()
            stop += 1
            if stop > 500:
                break


if __name__ == "__main__":
    main()