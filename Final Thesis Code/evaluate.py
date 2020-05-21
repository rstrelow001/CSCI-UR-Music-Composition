import tensorflow as tf
import pickle, os
from datetime import datetime
import numpy as np
from midiutil import MIDIFile
from _collections import defaultdict
import random
from tensorflow.keras.utils import to_categorical
import random, sys
def main():

    choice = input("sep or com or pitch?")
    args = sys.argv[1:]
    if choice == "sep":
        pickle_in = open("pickles/pitch_network_input_" + args[1] + "_" + args[0] + "_" + args[2] + "_normalized.pickle","rb")
        X = pickle.load(pickle_in)

        pickle_in = open("pickles/pitch_network_" + args[1] + "_" + args[0] + "_" + args[2] +"_labels.pickle","rb")
        y = pickle.load(pickle_in)

        pickle_in = open("pickles/pitch_types_" + args[1] + "_" + args[0] + "_" + args[2] + ".pickle","rb")
        pitch_types = pickle.load(pickle_in)
        print(pitch_types)

        y = np.array(y)
        #y = to_categorical(y)
        X = np.array(X)

        #model = tf.keras.models.load_model('logs/hparam_tuning_02-28-2020_09:57/run-0/trained_model.h5')

        if args[1] == "chinese":
            pitch_model = tf.keras.models.load_model(
                'logs/cutoff_1000/generator_128_chinese_Sequence_100_pitch/0.44349477_Epochs_30_trained_model_elasped_time_4678.h5')
            duration_model = tf.keras.models.load_model(
                'logs/cutoff_1000/generator_32_chinese_Sequence_100_duration/0.6693873_Epochs_10_trained_model_elasped_time_873.h5')
        else:
            pitch_model = tf.keras.models.load_model('logs/cutoff_5000/generator_128_german_Sequence_100_pitch/0.46745062_Epochs_30_trained_model_elasped_time_6909.h5')
            duration_model = tf.keras.models.load_model(
            'logs/cutoff_5000/generator_32_german_Sequence_100_duration/0.72919524_Epochs_30_trained_model_elasped_time_5771.h5')

        pickle_in = open("pickles/duration_network_input_"  + args[1] + "_" + args[0] + "_" + args[2] +"_normalized.pickle", "rb")
        all_durations = pickle.load(pickle_in)

        pickle_in = open("pickles/duration_network_" + args[1] + "_" + args[0] + "_" + args[2] +"_labels.pickle", "rb")
        all_durations_labels = pickle.load(pickle_in)

        pickle_in = open("pickles/duration_types_" + args[1] + "_" + args[0] + "_" + args[2] +".pickle","rb")
        duration_types = pickle.load(pickle_in)
        print(duration_types)
        all_durations_labels = np.array(all_durations_labels)
        # y = to_categorical(y)
        all_durations = np.array(all_durations)

        # model = tf.keras.models.load_model('logs/hparam_tuning_02-28-2020_09:57/run-0/trained_model.h5')

        #val_loss, val_acc = model.evaluate(X, y)

        #print("Validation Loss:\t" +str(val_loss))
        #print("Validation Accuracy:\t" + str(val_acc))



        current_directory = os.getcwd()
        midi_output_directory = os.path.join(current_directory, r'midi_output/separate_' + args[1] + "_" + datetime.now().strftime('%m-%d-%Y_%H:%M'))
        if not os.path.exists(midi_output_directory):
            os.makedirs(midi_output_directory)
        sum = 0
        count = 0
        breakpoint = 0
        final_pitches = []

        for i, label in zip(X, y):
            sample = i
            output = []
            for k in range(100):

                prediction = pitch_model.predict(np.reshape(sample, (1, 100, 1)))
                mod = np.array(prediction[0])
                top_4 = np.argpartition(mod, -2)[-2:]
                next = random.randint(0,1)
                adjusted = top_4[next]/(len(pitch_types)-1)
                sample = sample[1:]
                sample = np.append(sample, adjusted)
                output.append(top_4[next])

            print(output)
            final_pitches.append(output)
            #convert_to_midi(output, breakpoint, midi_output_directory)
            breakpoint += 1
            if breakpoint >= 50:
                break
        breakpoint = 0
        final_durations = []
        for i, label in zip(all_durations, all_durations_labels):
            sample = i
            output = []
            for k in range(100):

                prediction = duration_model.predict(np.reshape(sample, (1, 100, 1)))
                mod = np.array(prediction[0])
                top_4 = np.argpartition(mod, -2)[-2:]
                next = random.randint(0,1)
                adjusted = top_4[next]/(len(duration_types)-1)
                sample = sample[1:]
                sample = np.append(sample, adjusted)
                output.append(top_4[next])

            print(output)
            final_durations.append(output)
            #convert_to_midi(output, breakpoint, midi_output_directory)
            breakpoint += 1
            if breakpoint >= 50:
                break
        count = 0
        pickle_out = open("pickles/" + args[1] + "_pitch_Sequence_" + args[0] + "_CLASSIFICATION.pickle", "wb")
        pickle.dump(np.array(final_pitches), pickle_out)
        pickle_out.close()

        pickle_out = open("pickles/" + args[1] + "_duration_Sequence_" + args[0] + "_CLASSIFICATION.pickle", "wb")
        pickle.dump(np.array(final_durations), pickle_out)
        pickle_out.close()
        for pitches, durations in zip(final_pitches, final_durations):
            convert_to_midi(pitches, pitch_types, durations, duration_types, count, midi_output_directory)
            count += 1
    elif choice == "pitch":
        pickle_in = open(
            "pickles/pitch_network_input_" + args[1] + "_" + args[0] + "_" + args[2] + "_normalized.pickle", "rb")
        X = pickle.load(pickle_in)

        pickle_in = open("pickles/pitch_network_" + args[1] + "_" + args[0] + "_" + args[2] + "_labels.pickle", "rb")
        y = pickle.load(pickle_in)

        pickle_in = open("pickles/pitch_types_" + args[1] + "_" + args[0] + "_" + args[2] + ".pickle", "rb")
        pitch_types = pickle.load(pickle_in)
        print(pitch_types)

        y = np.array(y)
        # y = to_categorical(y)
        X = np.array(X)

        # model = tf.keras.models.load_model('logs/hparam_tuning_02-28-2020_09:57/run-0/trained_model.h5')
        if args[1] == "german":
            pitch_model = tf.keras.models.load_model('logs/cutoff_5000/generator_128_german_Sequence_100_pitch/0.46745062_Epochs_30_trained_model_elasped_time_6909.h5')
        else:
            pitch_model = tf.keras.models.load_model("logs/cutoff_1000/generator_128_chinese_Sequence_100_pitch/0.44349477_Epochs_30_trained_model_elasped_time_4678.h5")

        current_directory = os.getcwd()
        midi_output_directory = os.path.join(current_directory,
                                             r'midi_output/pitch_'+ args[1] + "_" + datetime.now().strftime('%m-%d-%Y_%H:%M'))
        if not os.path.exists(midi_output_directory):
            os.makedirs(midi_output_directory)
        sum = 0
        count = 0
        breakpoint = 0
        final_pitches = []
        final_durations = []
        for i, label in zip(X, y):
            sample = i
            output = []
            constant_duration = []
            for k in range(100):
                prediction = pitch_model.predict(np.reshape(sample, (1, 100, 1)))
                mod = np.array(prediction[0])
                top_4 = np.argpartition(mod, -2)[-2:]
                next = random.randint(0, 1)
                adjusted = top_4[next] / (len(pitch_types) - 1)
                sample = sample[1:]
                sample = np.append(sample, adjusted)
                output.append(top_4[next])
                constant_duration.append(4)

            print(output)
            final_pitches.append(output)
            final_durations.append(constant_duration)
            # convert_to_midi(output, breakpoint, midi_output_directory)
            breakpoint += 1
            if breakpoint >= 500:
                break
        duration_types = defaultdict(list)
        duration_types[4] = '4'
        count = 0
        pickle_out = open("pickles/" + args[1] + "_pitch_Sequence_" + args[0] + "_CLASSIFICATION.pickle", "wb")
        pickle.dump(np.array(final_pitches), pickle_out)
        pickle_out.close()
        for pitches, durations in zip(final_pitches, final_durations):
            convert_to_midi(pitches, pitch_types, durations, duration_types, count, midi_output_directory)
            count += 1

    elif choice == "com":
        pickle_in = open(
            "pickles/note_network_input_" + args[1] + "_" + args[0] + "_" + args[2] + "_normalized.pickle", "rb")
        X = pickle.load(pickle_in)

        pickle_in = open("pickles/note_network_" + args[1] + "_" + args[0] + "_" + args[2] + "_labels.pickle", "rb")
        y = pickle.load(pickle_in)

        pickle_in = open("pickles/note_types_" +  args[1] + "_" + args[0] + "_" + args[2] + ".pickle", "rb")
        note_types = pickle.load(pickle_in)

        y = np.array(y)
        # y = to_categorical(y)
        X = np.array(X)

        if args[1] == "german":
            note_model = tf.keras.models.load_model('logs/cutoff_500/generator_256_german_Sequence_100_note/0.4992405_Epochs_30_trained_model_elasped_time_7805.h5')
        else:
            note_model = tf.keras.models.load_model(
            'logs/cutoff_100/generator_256_chinese_Sequence_100_note/0.4544477_Epochs_30_trained_model_elasped_time_6620.h5')

        current_directory = os.getcwd()
        midi_output_directory = os.path.join(current_directory,
                                             r'midi_output/note_' + args[1] + "_" + datetime.now().strftime('%m-%d-%Y_%H:%M'))
        if not os.path.exists(midi_output_directory):
            os.makedirs(midi_output_directory)
        sum = 0
        count = 0
        breakpoint = 0
        final_notes = []


        for i, label in zip(X, y):
            sample = i
            output = []
            for k in range(100):
                prediction = note_model.predict(np.reshape(sample, (1, 100, 1)))
                mod = np.array(prediction[0])
                top_4 = np.argpartition(mod, -2)[-2:]
                next = random.randint(0, 1)
                adjusted = top_4[next] / (len(note_types) - 1)
                sample = sample[1:]
                sample = np.append(sample, adjusted)
                output.append(top_4[next])

            print(output)
            final_notes.append(output)
            # convert_to_midi(output, breakpoint, midi_output_directory)
            breakpoint += 1
            if breakpoint >= 50:
                break

        midi_pitches = {'GG': 43, 'AA': 45, 'G': 55, 'C': 48, 'D': 50, 'E': 52, 'F': 53,
                        'A': 57, 'B': 59, 'c': 60, 'd': 62, 'e': 64, 'f': 65,
                        'g': 67, 'a': 69, 'b': 71, 'cc': 72, 'dd': 74, 'ee': 76,
                        'ff': 77, 'gg': 79, 'aa': 81, 'bb': 83, 'ccc': 84, 'ddd': 86, 'eee': 88,
                        'fff': 89}
        num = 0
        pickle_out = open("pickles/" + args[1] + "_note_Sequence_" + args[0] + "_CLASSIFICATION.pickle", "wb")
        pickle.dump(np.array(final_notes), pickle_out)
        pickle_out.close()
        for notes in final_notes:

            track = 0
            channel = 0
            time = 0  # In beats
            duration = 0.5  # In beats
            tempo = 60  # In BPM
            volume = 100  # 0-127, as per the MIDI standard
            # pitches = [59,55,69,71,60,62,64,65,67,72,74,76,77,79]
            # pitches = [69, 60, 62, 64, 67,81, 72, 74, 76, 79]
            # for i, individual in enumerate(individuals):
            MyMIDI = MIDIFile(1, eventtime_is_ticks=True)  # One track, defaults to format 1 (tempo track is created
            # automatically)
            MyMIDI.addTempo(track, time, tempo)

            current_time_pos = 0
            current_pitch = 60
            for note in notes:
                split_note = note_types[note].split("_")
                absolute_duration = split_note[1]
                p = split_note[0]
                multiplier = 1
                if ".." in absolute_duration:
                    absolute_duration = absolute_duration[:-2]
                    multiplier = 1.75
                elif '.' in absolute_duration:
                    absolute_duration = absolute_duration[:-1]
                    multiplier = 1.5

                duration = 1 / int(absolute_duration) * 4 * multiplier
                # if note is not rest
                if p != "r":
                    next_pitch = midi_pitches.get(p)
                    try:
                        new = int(next_pitch)
                    except:
                        print(p)
                        print(next_pitch)
                    MyMIDI.addNote(track, channel, next_pitch, int(current_time_pos * 960), int(duration * 960), volume)

                current_time_pos += duration

            with open(midi_output_directory + "/Individual-" + str(num) + ".mid",
                      "wb") as output_file:
                MyMIDI.writeFile(output_file)
            num += 1

def convert_to_midi(pitches, pitch_dict, durations, duration_dict, num, output_path):
    midi_pitches = {'GG': 43, 'AA': 45, 'G': 55, 'C': 48, 'D': 50, 'E': 52, 'F': 53,
                    'A': 57, 'B': 59, 'c': 60, 'd': 62, 'e': 64, 'f': 65,
                    'g': 67, 'a': 69, 'b': 71, 'cc': 72, 'dd': 74, 'ee': 76,
                    'ff': 77, 'gg': 79, 'aa': 81, 'bb': 83, 'ccc': 84, 'ddd': 86, 'eee': 88,
                    'fff': 89}
    track = 0
    channel = 0
    time = 0  # In beats
    duration = 0.5  # In beats
    tempo = 60  # In BPM
    volume = 100  # 0-127, as per the MIDI standard
    #pitches = [59,55,69,71,60,62,64,65,67,72,74,76,77,79]
    #pitches = [69, 60, 62, 64, 67,81, 72, 74, 76, 79]
    #for i, individual in enumerate(individuals):
    MyMIDI = MIDIFile(1, eventtime_is_ticks=True)  # One track, defaults to format 1 (tempo track is created
        # automatically)
    MyMIDI.addTempo(track, time, tempo)

    current_time_pos = 0
    current_pitch = 60
    for p, d in zip(pitches, durations):
        absolute_duration = duration_dict[d]
        multiplier = 1
        if ".." in absolute_duration:
            absolute_duration = absolute_duration[:-2]
            multiplier = 1.75
        elif '.' in absolute_duration:
            absolute_duration = absolute_duration[:-1]
            multiplier = 1.5

        duration = 1 / int(absolute_duration) * 4 * multiplier
            # if note is not rest
        if p != 0:
            absolute_pitch = pitch_dict[p]
            next_pitch = midi_pitches.get(absolute_pitch)
            try:
                new = int(next_pitch)
            except:
                print(p)
                print(absolute_pitch)
                print(next_pitch)
            MyMIDI.addNote(track, channel, next_pitch, int(current_time_pos * 960), int(duration * 960), volume)

        current_time_pos += duration

    with open(output_path + "/Individual-" + str(num) + ".mid",
                  "wb") as output_file:
        MyMIDI.writeFile(output_file)

if __name__ == "__main__":
    main()