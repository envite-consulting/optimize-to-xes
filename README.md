# Optimize to XES

Export [raw data from Camunda Optimize](https://docs.camunda.io/optimize/apis-tools/optimize-api/report/get-data-export/) 
and convert it to [XES](https://xes-standard.org/) for Predictive Process Monitoring

# Table of Contents

* [✨Features](#features)
  * [📤 Camunda Optimize to XES](#-camunda-optimize-to-xes)
  * [📊 XES - eXtensible Event Stream](#-xes---extensible-event-stream)
  * [🚫 Limitations](#-limitations)
  * [🚀🔜 Coming Soon... 🌟🎉👀](#-coming-soon-)
* [🚀 Getting Started](#-getting-started)
  * [Obtaining a Bearer Token from Camunda Optimize](#obtaining-a-bearer-token-from-camunda-optimize)
  * [Configuring `application.yaml`](#configuring-applicationyaml)

# ✨Features

## 📤 Camunda Optimize to XES

Here's the scoop on this project: It's your ticket to export your raw process data from Camunda Optimize using the 
mighty [Data Export API](https://docs.camunda.io/optimize/apis-tools/optimize-api/report/get-data-export/). 🚀 
Afterward, we work our magic to transform it into the fantastic world of [XES](#-xes---extensible-event-stream). 🪄✨

## 📊 XES - eXtensible Event Stream

We rely on [XES](https://www.xes-standard.org/openxes/start), which is like a superhero cape for event data! 🦸‍♂️ 
It's a widely recognized standard that helps us structure and store event data in a machine-friendly format. This 
format is a go-to choice for process mining, allowing us to save and share event logs effortlessly. It's like a 
treasure map for uncovering insights in the world of processes and workflows! 🗺️

For our Java-powered adventures, we especially use this [OpenXES](http://code.deckfour.org/xes/) implementation. 🧑‍💻

## 🚫 Limitations

🛑 Currently, you can only transform one process definition at a time. Therefore, when setting up your raw data report 
in Camunda Optimize, make sure it contains only a single process definition.

## 🚀🔜 Coming Soon... 🌟🎉👀

![Optimize CCR PPM Cycle](./assets/ppm-cycle.png)

# 🚀 Getting Started

## Obtaining a Bearer Token from Camunda Optimize

To acquire a Bearer Token from Camunda Optimize, you can use the following curl command:

```shell
curl --request POST \
  --url https://login.cloud.camunda.io/oauth/token \
  --header 'Content-Type: application/json' \
  --data '{
    "client_id": "<client_id>",
    "client_secret": "<client_secret>",
    "audience
```

## Configuring `application.yaml`

To set up your configuration in `application.yaml`, follow these steps:

1. Add the Bearer Token you obtained earlier.
2. Include the Optimize base URL.
3. Specify the Raw Data Report ID.
4. Specify the filename for the resulting XML (optionally, along with its path).

Once you've completed these configurations, you'll be prepared to retrieve the data and convert it to XES format 🎉

To install all dependencies, run the following command:

```shell
$ ./mvnw install
```

To install and start the commandline runner, use the command below:

```shell
$ ./mvnw spring-boot:run
```