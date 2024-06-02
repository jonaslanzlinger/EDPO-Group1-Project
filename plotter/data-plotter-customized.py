import json
from datetime import datetime

import matplotlib.pyplot as plt
import matplotlib.dates as mdates

# Path to your file
file_path = './data/unload-blue_log_20240425-155908.txt'

HBW_1_data = {
    'hbw-timestamp': [],
    # 'i2_light_barrier': [],
    # 'i3_light_barrier': [],
    # 'i5_pos_switch': [],
    # 'i6_pos_switch': [],
    # 'i7_pos_switch': [],
    # 'i8_pos_switch': [],
    'hbw-current_pos_x': [],
    'hbw-current_pos_y': [],
    'hbw-i1_light_barrier': [],
    'hbw-i4_light_barrier': [],
    # 'target_pos_x': [],
    # 'target_pos_y': [],
    'vgr-timestamp': [],
    #'i1_pos_switch': [],
    #'i2_pos_switch': [],
    #'i3_pos_switch': [],
    #'vgr-i7_light_barrier': [],
    #'vgr-i4_light_barrier': [],
    'vgr-i8_color_sensor': [],
    'vgr-o7_compressor_level': [],
    #'vgr-o8_valve_open': [],
    #'vgr-m1_speed': [],
    #'vgr-m2_speed': [],
    #'vgr-m3_speed': [],
    #'current_state': [],
    # 'current_task': [],
    # 'current_task_duration': [],
    # 'current_sub_task': [],
    'vgr-current_pos_x': [],
    'vgr-current_pos_y': [],
    #'vgr-current_pos_z': [],
    #'vgr-target_pos_x': [],
    #'vgr-target_pos_y': [],
    #'vgr-target_pos_z': []
}

VGR_1_data = {
    'vgr-timestamp': [],
    #'i1_pos_switch': [],
    #'i2_pos_switch': [],
    #'i3_pos_switch': [],
    # 'i7_light_barrier': [],
    #'vgr-i4_light_barrier': [],
    'vgr-i8_color_sensor': [],
    'vgr-o7_compressor_level': []}
    #'vgr-o8_valve_open': [],
    #'vgr-m1_speed': [],
    #'vgr-m2_speed': [],
    #'vgr-m3_speed': [],
    #'current_state': [],
    # 'current_task': [],
    # 'current_task_duration': [],
    # 'current_sub_task': [],
    #'vgr-current_pos_x': [],
    #'vgr-current_pos_y': [],
    #'vgr-current_pos_z': [],
    #'vgr-target_pos_x': [],
    #'vgr-target_pos_y': [],
    #'vgr-target_pos_z': []}


def fill(data, json, prefix):
    for key in json:
        if prefix + key in data:
            if key == 'timestamp':
                # Convert the timestamp string to a datetime object
                data[prefix + key].append(datetime.strptime(json[key], '%Y-%m-%d %H:%M:%S.%f'))
            else:
                data[prefix + key].append(json[key])


def create_plots(data):
    plt.figure(figsize=(20, 20))
    # Set the figure size for better readability
    num_plots = len(data) - 1  # Subtract 1 to exclude 'timestamp'

    # Iterate over each key in the dictionary except 'timestamp'
    for index, (key, values) in enumerate(data.items()):
        if key[4:] == 'timestamp':
            continue  # Skip the timestamp key

        ax = plt.subplot(num_plots, 1, index)  # Create a subplot for each sensor/actuator
        if key == 'hbw-timestamp':
            ax.plot(data['hbw-timestamp'], values, label=key, marker='o')  # Plot data points
        else:
            ax.plot(data['vgr-timestamp'], values, label=key, marker='o')
        ax.set_title(key)  # Set the title to the name of the sensor/actuator
        ax.legend()
        ax.grid(True)

        # Set the format of the timestamp in the x-axisfrom datetime import datetime

        # "2024-04-25 15:59:10.58"
        ax.xaxis.set_major_formatter(mdates.DateFormatter('%Y-%m-%d %H:%M:%S'))
        ax.xaxis.set_major_locator(mdates.AutoDateLocator(maxticks=5))  # Improve formatting of timestamp labels

    plt.tight_layout()  # Adjust subplots to fit into the figure area
    plt.show()


# Open and read the file line by line
with open(file_path, 'r') as file:
    for line in file:
        try:
            # Parse the JSON string from each line
            json_data = json.loads(line)
            station = json_data['station']

            action = {
                'HBW_1': lambda x: fill(HBW_1_data, json_data, "hbw-"),
                'VGR_1': lambda x: fill(HBW_1_data, json_data, "vgr-"),
            }.get(station, lambda x: print('Station not found/omitted'))

            action(json_data)
        except json.JSONDecodeError:
            print('Error parsing JSON')


# Plot the data for each station
create_plots(HBW_1_data)
#create_plots(VGR_1_data)
