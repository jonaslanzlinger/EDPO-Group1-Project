# FactorySimulator Service
This service is responsible for simulating the factory environment. It is responsible for simulating the 
factory environment.
Because we are not able to have permanent access to the factory, we need to simulate the factory 
environment by reading a data dump file. This file can be found [here](src%2Fmain%2Fresources%2Fdata.txt).

## MQTT
Because the real smart factory is using MQTT to send data, we will also use MQTT to simulate the behavior 
of the factory. Therefore, the [MqttClient](src%2Fmain%2Fjava%2Forg%2Fexample%2Fmqtt%2FMqttClient.java) 
has been added to the service. We can use this client to send data to the MQTT broker (specified in the 
[application.yml](src%2Fmain%2Fresources%2Fapplication.yml) file).

## Start sending data
To start sending data from the factory simulator, we need to start the service. Then, we can call the 
endpoint `http://localhost:8085/send` to start sending data. The [FactoryRestController](src%2Fmain%2Fjava%2Forg%2Fexample%2Frest%2FFactoryRestController.java)
is responsible for handling this request. The controller will then read the data file and send the data 
line by line to the MQTT broker until the end of file has been reached. Note: For now, only messages to 
the stations HBW and VGR_1 are being sent, as we are not using all the other stations yet.

## Stations
With an eye on potential future developments, we have not only created station classes for the warehouse, 
the gripper, and the delivery & pickup station, but also created classes for all the other stations in the 
real factory. Those classes are implementing all the attributes that the real stations have. This way, we
can easily exchange the simulated factory with the real factory in the future.

## Simulating Interaction
For demonstrating purposes we have added the [HBWRestController](src%2Fmain%2Fjava%2Forg%2Fexample%2Frest%2FHBWRestController.java)
class that implements the exact same endpoints as the real HBW service. This way we can simulate the 
interaction between the factory simulator and a client. Note: We are not calling those endpoints yet.