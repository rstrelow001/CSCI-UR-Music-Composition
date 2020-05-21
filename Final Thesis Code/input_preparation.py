
from os import listdir
from os.path import isfile, join
import sys, glob, os, random, pickle
import numpy, random
from collections import defaultdict


def main():
# Txt read
    args = sys.argv[1:]
    #setup(args[0], args[1])
    read_new = input("Chosse an action: \n sep gen -> Create input for separate pitch and duration generation networks"
                     "\n sep class - > Create input for separate pitch and duration classification networks"
                     "\n com gen -> Create input for combined pitch and duration generation networks"
                     "\n com class -> Create input for combined pitch and duration classification networks\n")
    if read_new == "sep gen":
        # USAGE pitch_path      duration_path       sequence_length         genre       trim_amount
        max_file_length, min_file_length, mean_file_length = find_max_min_mean_length(args[0])
        print("Max File Length: %d\tMin File Length: %d\tMean File Length: %d" % (max_file_length, min_file_length, mean_file_length))

        pitch_types = unique_pitches(args[0])
        duration_types = unique_durations(args[1])
        print("Unique Pitches: %d" % len(pitch_types))
        print(pitch_types)
        print("Unique Durations: %d" % len(duration_types))
        print(duration_types)
        all_songs, pitch_dict, duration_songs, duration_dict = read_all(pitch_types, args[0], duration_types, args[1])
        network_input, network_labels, label_occurence = create_input(all_songs, int(args[2]), len(pitch_dict.keys()))#len(pitch_types) * len(duration_types) - 1)
        duration_network_input, duration_network_labels, duration_label_occurence = create_input(duration_songs, int(args[2]), len(duration_dict.keys()))

        print(len(network_labels))
        print(len(network_input))

        training_data = []

        for i, l in zip(network_input, network_labels):
            training_data.append([i, l])
        print("Pre trimmed: %d" % len(training_data))
        training_data, to_keep = trim_input(training_data, pitch_dict, int(args[4]))
        print("Trimmed: %d" % len(training_data))
        #----------------------------------------------------------------------------------

        new_pitch_types = []
        for index in to_keep:
            new_pitch_types.append(pitch_types[index])
        print(len(new_pitch_types))
        print(new_pitch_types)

        temp_data = []

        test_breakpoint = 0
        for sample, label in training_data:
            new_sample = []

            for s in sample:
                temp = pitch_types[int(s)]
                new_sample.append(new_pitch_types.index(temp))
            temp_label = pitch_types[int(label)]
            temp_data.append([new_sample, new_pitch_types.index(temp_label)])
            test_breakpoint += 1

        pickle_out = open("pickles/pitch_types_"+ args[3] + "_" + args[2] + "_" + args[4] + ".pickle", "wb")
        pickle.dump(new_pitch_types, pickle_out)
        pickle_out.close()

        training_data = temp_data

        random.shuffle(training_data)

        print(training_data[:3])

        X = []
        y = []


        for features, label in training_data:

            X.append(features)
            y.append(label)

        print(X[:3])
        print(y[:3])
        X = numpy.reshape(X, (len(X), len(X[0]), 1))
        X = X / (len(new_pitch_types)-1)

        pickle_out = open("pickles/pitch_network_input_" + args[3] + "_" + args[2] + "_" + args[4] + "_normalized.pickle", "wb")
        pickle.dump(X, pickle_out)
        pickle_out.close()

        pickle_out = open("pickles/pitch_network_"+ args[3] + "_" + args[2] + "_" + args[4] + "_labels.pickle", "wb")
        pickle.dump(y, pickle_out)
        pickle_out.close()



        #____________________________DURATION INPUT_______________________________________________________________

        training_data = []

        for i, l in zip(duration_network_input, duration_network_labels):
            training_data.append([i, l])
        print("Pre trimmed: %d" % len(training_data))
        training_data, to_keep = trim_input(training_data, duration_dict, int(args[4]))
        print("Trimmed: %d" % len(training_data))
        # ----------------------------------------------------------------------------------

        new_duration_types = []
        for index in to_keep:
            new_duration_types.append(duration_types[index])
        print(len(new_duration_types))
        print(new_duration_types)

        temp_data = []

        test_breakpoint = 0
        for sample, label in training_data:
            new_sample = []

            for s in sample:
                temp = duration_types[int(s)]
                new_sample.append(new_duration_types.index(temp))
            temp_label = duration_types[int(label)]
            temp_data.append([new_sample, new_duration_types.index(temp_label)])
            test_breakpoint += 1

        pickle_out = open("pickles/duration_types_" + args[3] + "_" + args[2] + "_" + args[4] + ".pickle", "wb")
        pickle.dump(new_duration_types, pickle_out)
        pickle_out.close()

        training_data = temp_data

        random.shuffle(training_data)

        print(training_data[:3])

        X = []
        y = []

        for features, label in training_data:
            X.append(features)
            y.append(label)

        print(X[:3])
        print(y[:3])
        X = numpy.reshape(X, (len(X), len(X[0]), 1))
        X = X / (len(new_duration_types) - 1)

        pickle_out = open("pickles/duration_network_input_" + args[3] + "_" + args[2] + "_" + args[4] + "_normalized.pickle", "wb")
        pickle.dump(X, pickle_out)
        pickle_out.close()

        pickle_out = open("pickles/duration_network_" + args[3] + "_" + args[2] + "_" + args[4] + "_labels.pickle", "wb")
        pickle.dump(y, pickle_out)
        pickle_out.close()

    elif read_new == "com class": #Combined for classification
        #USAGE class1_pitch_path        class1_duration_path       class2_pitch_path        class2_duration_path        sequence length
        pitch_types = unique_pitches(args[0])
        other_pitch_types = unique_pitches(args[2])

        for pitch in other_pitch_types:
            if pitch not in pitch_types:
                pitch_types.append(pitch)

        duration_types = unique_durations(args[1])
        other_duration_types = unique_durations(args[3])
        for duration in other_duration_types:
            if duration not in duration_types:
                duration_types.append(duration)

        print("Unique Pitches: %d" % len(pitch_types))
        print(pitch_types)
        print("Unique Durations: %d" % len(duration_types))
        print(duration_types)

        note_types = []

        for pitch in pitch_types:
            for duration in duration_types:
                note_types.append(pitch + "_" + str(duration))

        all_songs_class1, note_dict_class1 = read_all_combined(note_types, args[0], args[1])
        all_songs_class2, note_dict_class2 = read_all_combined(note_types, args[2], args[3])
        network_class1, _, _ = create_input(all_songs_class1, int(args[4]), len(note_dict_class1.keys()))  # len(pitch_types) * len(duration_types) - 1)
        network_class2, _, _ = create_input(all_songs_class2, int(args[4]), len(note_dict_class2.keys()))
        '''
        note_dict = defaultdict(list)
        for key in note_dict_class1.keys():
            note_dict[key] = note_dict_class1.get(key) + note_dict_class2.get(key)
        '''


        temp_data = []

        for i in range(len(network_class1)):
            temp_data.append([network_class1[i], 0])
        for j in range(len(network_class2)):
            temp_data.append([network_class2[j], 1])



        pickle_out = open("pickles/classifier_note_types_" + args[4] + "_" + ".pickle", "wb")
        pickle.dump(note_types, pickle_out)
        pickle_out.close()

        random.shuffle(temp_data, seed)

        class_1_count = 0
        class_2_count = 1
        eval_data = []
        training_data = []

        for k in range(len(temp_data)):
            print(temp_data[k][1])
            if temp_data[k][1] == 0:
                if class_1_count < 75000:
                    training_data.append(temp_data[k])
                    class_1_count += 1
                else:
                    eval_data.append(temp_data[k])
            else:
                if class_2_count < 75000:
                    training_data.append(temp_data[k])
                    class_2_count += 1
                else:
                    eval_data.append(temp_data[k])

        print(training_data[:3])

        random.shuffle(eval_data)
        X = []
        y = []

        eval_X = []
        eval_y = []
        for features, label in training_data:
            X.append(features)
            y.append(label)

        for feature, label in eval_data:
            eval_X.append(feature)
            eval_y.append(label)

        print(X[:5])
        print(y[:5])

        X = numpy.reshape(X, (len(X), len(X[0]), 1))
        X = X / (len(note_types) - 1)
        eval_X = numpy.reshape(eval_X, (len(eval_X), len(eval_X[0]), 1))
        eval_X = eval_X / (len(note_types) - 1)

        pickle_out = open("pickles/classifier_note_network_input_" + args[4]  + "_normalized.pickle", "wb")
        pickle.dump(X, pickle_out)
        pickle_out.close()

        pickle_out = open("pickles/classifier_note_network_" + args[4] + "_labels.pickle", "wb")
        pickle.dump(y, pickle_out)
        pickle_out.close()

        pickle_out = open("pickles/classifier_note_network_input_" + args[4] +  "_normalized_EVALUATION.pickle", "wb")
        pickle.dump(eval_X, pickle_out)
        pickle_out.close()

        pickle_out = open("pickles/classifier_note_network_" + args[4] + "_labels_EVALUATION.pickle", "wb")
        pickle.dump(eval_y, pickle_out)
        pickle_out.close()

    elif read_new == "com gen": #Combined for generation
        # USAGE pitch_path      duration_path       sequence_length         genre       trim_amount
        note_types = unique_notes(args[0], args[1])

        notes, dict_notes = read_all_combined(note_types, args[0], args[1])

        network_input, network_labels, label_occurence = create_input(notes,int(args[2]), len(dict_notes.keys()))

        training_data = []

        for i, l in zip(network_input, network_labels):
            training_data.append([i, l])
        print("Pre trimmed: %d" % len(training_data))
        training_data, to_keep = trim_input(training_data, dict_notes, int(args[4]))
        print("Trimmed: %d" % len(training_data))
        # ----------------------------------------------------------------------------------

        new_note_types = []
        for index in to_keep:
            new_note_types.append(note_types[index])
        print(len(new_note_types))
        print(new_note_types)

        temp_data = []

        test_breakpoint = 0
        for sample, label in training_data:
            new_sample = []

            for s in sample:
                temp = note_types[int(s)]
                new_sample.append(new_note_types.index(temp))
            temp_label = note_types[int(label)]
            temp_data.append([new_sample, new_note_types.index(temp_label)])
            test_breakpoint += 1

        pickle_out = open("pickles/note_types_" + args[3] + "_" + args[2] + "_" + args[4] + ".pickle", "wb")
        pickle.dump(new_note_types, pickle_out)
        pickle_out.close()

        training_data = temp_data

        random.shuffle(training_data)

        print(training_data[:3])

        X = []
        y = []

        for features, label in training_data:
            X.append(features)
            y.append(label)

        print(X[:3])
        print(y[:3])
        X = numpy.reshape(X, (len(X), len(X[0]), 1))
        X = X / (len(new_note_types) - 1)

        pickle_out = open("pickles/note_network_input_" + args[3] + "_" + args[2] + "_" + args[4] + "_normalized.pickle", "wb")
        pickle.dump(X, pickle_out)
        pickle_out.close()

        pickle_out = open("pickles/note_network_" + args[3] + "_" + args[2] + "_" + args[4] + "_labels.pickle", "wb")
        pickle.dump(y, pickle_out)
        pickle_out.close()

    elif read_new == "sep class":
        #USAGE class1_pitch_path    class1_duration_path     class2_pitch_path   class2_duration_path   sequence length

        pitch_types = unique_pitches(args[0])
        other_pitch_types = unique_pitches(args[2])

        for pitch in other_pitch_types:
            if pitch not in pitch_types:
                pitch_types.append(pitch)

        duration_types = unique_durations(args[1])
        other_duration_types = unique_durations(args[3])
        for duration in other_duration_types:
            if duration not in duration_types:
                duration_types.append(duration)

        print("Unique Pitches: %d" % len(pitch_types))
        print(pitch_types)
        print("Unique Durations: %d" % len(duration_types))
        print(duration_types)

        all_songs_class1, pitch_dict_class1, duration_songs_class1, duration_dict_class1 = read_all(pitch_types, args[0], duration_types, args[1])

        all_songs_class2, pitch_dict_class2, duration_songs_class2, duration_dict_class2 = read_all(pitch_types, args[2], duration_types, args[3])
        network_class1, _, _ = create_input(all_songs_class1, int(args[4]), len(pitch_dict_class1.keys()))  # len(pitch_types) * len(duration_types) - 1)
        network_class2, _, _ = create_input(all_songs_class2, int(args[4]), len(pitch_dict_class2.keys()))
        duration_network_class1, _, _ = create_input(duration_songs_class1, int(args[4]), len(duration_dict_class1.keys()))
        duration_network_class2, _, _ = create_input(duration_songs_class2, int(args[4]), len(duration_dict_class2.keys()))

        pitch_dict = defaultdict(list)
        for key in pitch_dict_class1.keys():
            pitch_dict[key] = pitch_dict_class1.get(key) + pitch_dict_class2.get(key)

        duration_dict = defaultdict(list)
        for key in duration_dict_class1.keys():
            duration_dict[key] = duration_dict_class1.get(key) + duration_dict_class2.get(key)

        '''
        print(len(network_class1))
        print(len(network_class2))
        input()
        '''

        temp_data = []

        for i in range(len(network_class1)):
            temp_data.append([network_class1[i], 0])
        for j in range(len(network_class2)):
            temp_data.append([network_class2[j], 1])



        pickle_out = open("pickles/classifier_pitch_types_" + args[4] + ".pickle", "wb")
        pickle.dump(pitch_types, pickle_out)
        pickle_out.close()


        random.shuffle(temp_data, seed)

        class_1_count = 0
        class_2_count = 1
        training_data = []
        eval_data = []

        for k in range(len(temp_data)):
            print(temp_data[k][1])
            if temp_data[k][1] == 0:
                if class_1_count < 75000:
                    training_data.append(temp_data[k])
                    class_1_count += 1
                else:
                    eval_data.append(temp_data[k])
            else:
                if class_2_count < 75000:
                    training_data.append(temp_data[k])
                    class_2_count += 1
                else:
                    eval_data.append(temp_data[k])

        print(training_data[:3])

        random.shuffle(eval_data)
        X = []
        y = []

        eval_X = []
        eval_y = []
        for features, label in training_data:
            X.append(features)
            y.append(label)

        for feature, label in eval_data:
            eval_X.append(feature)
            eval_y.append(label)

        print(X[:5])
        print(y[:5])

        X = numpy.reshape(X, (len(X), len(X[0]), 1))
        X = X / (len(pitch_types) - 1)
        eval_X = numpy.reshape(eval_X, (len(eval_X), len(eval_X[0]), 1))
        eval_X = eval_X / (len(pitch_types) - 1)


        pickle_out = open("pickles/classifier_pitch_network_input_" + args[4] +  "_normalized.pickle", "wb")
        pickle.dump(X, pickle_out)
        pickle_out.close()

        pickle_out = open("pickles/classifier_pitch_network_" + args[4] + "_labels.pickle", "wb")
        pickle.dump(y, pickle_out)
        pickle_out.close()

        pickle_out = open("pickles/classifier_pitch_network_input_" + args[4] + "_normalized_EVALUATION.pickle", "wb")
        pickle.dump(eval_X, pickle_out)
        pickle_out.close()

        pickle_out = open("pickles/classifier_pitch_network_" + args[4] + "_labels_EVALUATION.pickle", "wb")
        pickle.dump(eval_y, pickle_out)
        pickle_out.close()


        #Duration INPUT ------------------------------------------------------------------------------------
        temp_data = []

        for i in range(len(network_class1)):
            temp_data.append([duration_network_class1[i], 0])
        for j in range(len(network_class2)):
            temp_data.append([duration_network_class2[j], 1])

        pickle_out = open("pickles/classifier_duration_types_" + args[4] + ".pickle", "wb")
        pickle.dump(duration_types, pickle_out)
        pickle_out.close()

        random.shuffle(temp_data, seed)

        class_1_count = 0
        class_2_count = 1
        training_data = []
        eval_data = []

        for k in range(len(temp_data)):
            print(temp_data[k][1])
            if temp_data[k][1] == 0:
                if class_1_count < 75000:
                    training_data.append(temp_data[k])
                    class_1_count += 1
                else:
                    eval_data.append(temp_data[k])
            else:
                if class_2_count < 75000:
                    training_data.append(temp_data[k])
                    class_2_count += 1
                else:
                    eval_data.append(temp_data[k])

        print(training_data[:3])

        random.shuffle(eval_data)
        X = []
        y = []

        eval_X = []
        eval_y = []
        for features, label in training_data:
            X.append(features)
            y.append(label)

        for feature, label in eval_data:
            eval_X.append(feature)
            eval_y.append(label)

        print(X[:5])
        print(y[:5])

        X = numpy.reshape(X, (len(X), len(X[0]), 1))
        X = X / (len(duration_types) - 1)
        eval_X = numpy.reshape(eval_X, (len(eval_X), len(eval_X[0]), 1))
        eval_X = eval_X / (len(duration_types) - 1)

        pickle_out = open("pickles/classifier_duration_network_input_" + args[4] + "_normalized.pickle", "wb")
        pickle.dump(X, pickle_out)
        pickle_out.close()

        pickle_out = open("pickles/classifier_duration_network_" + args[4] + "_labels.pickle", "wb")
        pickle.dump(y, pickle_out)
        pickle_out.close()

        pickle_out = open("pickles/classifier_duration_network_input_" + args[4] + "_normalized_EVALUATION.pickle", "wb")
        pickle.dump(eval_X, pickle_out)
        pickle_out.close()

        pickle_out = open("pickles/classifier_duration_network_" + args[4] + "_labels_EVALUATION.pickle", "wb")
        pickle.dump(eval_y, pickle_out)
        pickle_out.close()

def seed():
    return 0.1

def find_max_min_mean_length(dir):

    max = 0
    min = 10000
    total_lines = 0.0
    total_files = 0.0

    if os.path.isdir(dir):

        text_files = glob.glob(dir + '/*.krn')
        for file in text_files:
            total_files += 1
            count = 0
            input1 = open(file, 'r', encoding = "ISO-8859-1").read().strip()
            lines = input1.split("\n")
            for line in lines:
                if line:
                    count += 1
            total_lines += count
            if count > max:
                max = count
            if count < min:
                min = count
    mean = total_lines/total_files
    return max, min, mean


def unique_pitches(dir):

    count = 0
    final_pitches = []
    if os.path.isdir(dir):
        text_files = glob.glob(dir + '/*.krn')
        print(len(text_files))
        for file in text_files:
            input1 = open(file, 'r', encoding = "ISO-8859-1").read().strip()
            pitches = input1.split("\n")

            for p in pitches:
                if p == "Errrercegg" or p == "Errrercecc" or p == "Errrerceff":
                    print(file)
                if p not in final_pitches and p != 'Errrercegg' and p != "Errrercecc" and p != "Errrerceff" and p != 'r':
                    final_pitches.append(p)
            count += 1
            #if count == 1000:
             #   break
    caps = []
    lowers = []
    for pitch in final_pitches:
        if pitch.isupper():
            caps.append(pitch)
        else:
            lowers.append(pitch)

    return ['r'] + sorted(caps, key=lambda x: (-len(x),x)) + sorted(lowers, key=lambda x: (len(x),x))


def unique_durations(dir):

    count = 0
    final_durations = []
    if os.path.isdir(dir):
        text_files = glob.glob(dir + '/*.krn')
        print(len(text_files))
        for file in text_files:
            input1 = open(file, 'r', encoding = "ISO-8859-1").read().strip()
            durations = input1.split("\n")

            for d in durations:
                try:
                    #catch errors with durations like 1.5 for example
                    index = d.index('.')
                    if index != -1:
                        d = d[:index+1]
                except:
                    pass
                if d not in final_durations and d != "":
                    final_durations.append(d)
            count += 1
            #if count == 1000:
            #    break
    return sorted(final_durations, key=lambda x: (float(x), x))


def unique_notes(pitch_dir, duration_dir):
    count = 0
    final_notes = []
    if os.path.isdir(pitch_dir) and os.path.isdir(duration_dir):
        pitch_files = glob.glob(pitch_dir + '/*.krn')
        duration_files = glob.glob(duration_dir + '/*.krn')
        for p_file, d_file in zip(pitch_files, duration_files):
            input1 = open(p_file, 'r', encoding="ISO-8859-1").read().strip()
            input2 = open(d_file, 'r', encoding="ISO-8859-1").read().strip()
            pitches = input1.split("\n")
            durations = input2.split("\n")
            for p, d in zip(pitches, durations):
                note = p + "_" + str(d)
                if p == "Errrercegg" or p == "Errrercecc" or p == "Errrerceff":
                    print(p_file)
                if note not in final_notes:
                    final_notes.append(note)
            count += 1
            # if count == 1000:
            #   break
    caps = []
    lowers = []
    rests = []
    for note in final_notes:
        if note.split('_')[0].isupper():
            caps.append(note)
        elif note.split('_')[0] == "r":
            rests.append(note)
        else:
            lowers.append(note)

    return sorted(rests) + sorted(caps, key=lambda x: (-len(x), x)) + sorted(lowers, key=lambda x: (len(x), x))


def read_all(pitch_types, pitch_dir, duration_types, duration_dir):
    count = 0
    final_notes = []
    final_durations = []

    unique_values = defaultdict(list)
    unique_durations = defaultdict(list)

    for i in range(len(pitch_types)):
        unique_values[i] = 0
    for i in range(len(duration_types)):
        unique_durations[i] = 0

    if os.path.isdir(pitch_dir) and os.path.isdir(duration_dir):
        pitch_files = glob.glob(pitch_dir + "/*.krn")
        duration_files = glob.glob(duration_dir + "/*.krn")
        for p_file, d_file in zip(pitch_files, duration_files):
            current_input = []
            duration_input = []
            input_p = open(p_file, 'r', encoding = "ISO-8859-1").read().strip()
            pitches = input_p.split("\n")

            input_d = open(d_file, 'r', encoding="ISO-8859-1").read().strip()
            durations = input_d.split("\n")

            for p, d in zip(pitches, durations):
                if d == "1.5":
                    d = "1."
                if pitch_types.index(p) not in unique_values.keys():
                    unique_values[pitch_types.index(p)] = 1
                else:
                    unique_values[pitch_types.index(p)] += 1
                if duration_types.index(d) not in unique_durations.keys():
                    unique_durations[duration_types.index(d)] = 1
                else:
                    unique_durations[duration_types.index(d)] += 1
                #current_input.append(pitch_types.index(p) + duration_types.index(d)*30)
                current_input.append(pitch_types.index(p))
                duration_input.append(duration_types.index(d))
            final_notes.append(current_input)
            final_durations.append(duration_input)
            count += 1
            #if count == 1000:
             #   break
    for key in unique_values.keys():
        print("Key: %d\t Value: %d" % (key, unique_values.get(key)))
    #input()

    return final_notes, unique_values, final_durations, unique_durations

def read_all_combined(note_types, pitch_dir, duration_dir):
    count = 0
    final_notes = []


    note_types_count = defaultdict(list)

    for i in range(len(note_types)):
        note_types_count[i] = 0

    if os.path.isdir(pitch_dir) and os.path.isdir(duration_dir):
        pitch_files = glob.glob(pitch_dir + "/*.krn")
        duration_files = glob.glob(duration_dir + "/*.krn")
        for p_file, d_file in zip(pitch_files, duration_files):
            current_input = []
            duration_input = []
            input_p = open(p_file, 'r', encoding = "ISO-8859-1").read().strip()
            pitches = input_p.split("\n")

            input_d = open(d_file, 'r', encoding="ISO-8859-1").read().strip()
            durations = input_d.split("\n")

            for p, d in zip(pitches, durations):
                if d == "1.5":
                    d = "1."
                note_types_count[note_types.index(p + "_" + d)] += 1
                current_input.append(note_types.index(p + "_" + d))

            final_notes.append(current_input)

            count += 1
            #if count == 1000:
             #   break
    for key in note_types_count.keys():
        print("Key: %d\t Value: %d" % (key, note_types_count.get(key)))

    return final_notes, note_types_count

def create_input(all_songs, sequence_length, dict_len):

    labels_count = defaultdict(list)
    for i in range(dict_len):
        labels_count[i] = 0

    network_input = []

    network_labels = []

    combined = []
    for song in all_songs:
        for note in song:
            combined.append(note)

    for i in range(0, len(combined) - sequence_length, 1):
        sequence_in = combined[i:i + sequence_length]
        out_label = combined[i + sequence_length]
        network_input.append(sequence_in)
        network_labels.append(out_label)
        labels_count[int(out_label)] += 1
    '''
    for song in all_songs:
        for i in range(0, len(song)- sequence_length, 1):
            sequence_in = song[i:i + sequence_length]
            out_label = song[i+sequence_length]
            network_input.append(sequence_in)
            network_labels.append(out_label)
    '''

    n_patterns = len(network_input)

    #reshape input
    network_input = numpy.reshape(network_input, (n_patterns, sequence_length, 1))

    #normalize
    #network_input = network_input/ float(num_vocab)


    #network_labels = to_categorical(network_labels)

    return network_input, network_labels, labels_count


def trim_input(full_network_input, occurence_dict, size_limit):
    indices_to_remove = []
    to_keep = []
    for key in occurence_dict:
        if occurence_dict.get(key) < size_limit:
            indices_to_remove.append(key)
        else:
            to_keep.append(key)
    print(indices_to_remove)
    trimmed_input = []
    for sample, l in full_network_input:
        good = True
        for index in indices_to_remove:
            if index in sample or index == l:
                good = False
                break
        if good:
            trimmed_input.append([sample, l])
    print("Number of classes to keep: %d\tNumber to remove: %d\tKeep percentage: %.4f" % (len(to_keep), len(indices_to_remove), len(to_keep)/ (len(to_keep) + len(indices_to_remove))))
    print("Pretrimmed network: %d\tTrimmed network: %d\tKeep Percentage: %.4f" % (len(full_network_input), len(trimmed_input), len(trimmed_input)/ len(full_network_input)))
    return trimmed_input, sorted(to_keep)

def trim_input_classifier(full_network_input, occurence_dict, size_limit):
    indices_to_remove = []
    to_keep = []
    for key in occurence_dict:
        if occurence_dict.get(key) < size_limit:
            indices_to_remove.append(key)
        else:
            to_keep.append(key)
    print(indices_to_remove)
    trimmed_input = []
    for sample, l in full_network_input:
        good = True
        for index in indices_to_remove:
            if index in sample:
                good = False
                break
        if good:
            trimmed_input.append([sample, l])
    return trimmed_input, sorted(to_keep)


if __name__ == "__main__":
    main()
