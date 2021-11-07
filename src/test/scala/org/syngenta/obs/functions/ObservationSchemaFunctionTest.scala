package org.syngenta.obs.functions

import com.fasterxml.jackson.databind.ObjectMapper
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.flink.streaming.runtime.streamrecord.StreamRecord
import org.apache.flink.streaming.util.ProcessFunctionTestHarnesses
import org.scalatest.{FlatSpec, Matchers}
import org.syngenta.obs.data.ObservationData

import scala.org.syngenta.obs.functions.ObservationSchemaFunction
import scala.org.syngenta.obs.job.ObservationSchemaConfig

class ObservationSchemaFunctionTest extends FlatSpec with Matchers{

  val config: Config = ConfigFactory.load("obs-test.conf")
  val obsConfig: ObservationSchemaConfig = new ObservationSchemaConfig(config)

  //instantiate created user defined function
  val processFunction = new ObservationSchemaFunction(obsConfig)
  val objMapper = new ObjectMapper

  "ObservationSchemaFunction" should "write data into valid topic" in {
    // wrap user defined function into a the corresponding operator
    val harness = ProcessFunctionTestHarnesses.forProcessFunction(processFunction)


    //StreamRecord object with the sample data
    val testData: StreamRecord[String] = new StreamRecord[String](ObservationData.OBS_TEST_DATA)
    harness.processElement(testData)

    harness.getSideOutput(obsConfig.validObsOutputTag) should have size 1

    harness.close()
  }

  "ObservationSchemaFunction" should "write data into failed topic due to missing type in spatialExtent" in {
    // wrap user defined function into a the corresponding operator
    val harness = ProcessFunctionTestHarnesses.forProcessFunction(processFunction)


    //StreamRecord object with the sample data
    val testData: StreamRecord[String] = new StreamRecord[String](ObservationData.OBS_FAIL_SPATIAL_TYPE_DATA)
    harness.processElement(testData)

    harness.getSideOutput(obsConfig.failedObsOutputTag) should have size 1
    /***
    --- BEGIN MESSAGES ---
      error: object has missing required properties (["type"])
    level: "error"
    schema: {"loadingURI":"#","pointer":"/properties/spatialExtent"}
    instance: {"pointer":"/spatialExtent"}
    domain: "validation"
    keyword: "required"
    required: ["latCoordinates","lonCoordinates","type"]
    missing: ["type"]
    ---  END MESSAGES  ---
    ****/

    harness.close()
  }

  "ObservationSchemaFunction" should "write data into failed topic due to missing spatialExtent" in {
    // wrap user defined function into a the corresponding operator
    val harness = ProcessFunctionTestHarnesses.forProcessFunction(processFunction)


    //StreamRecord object with the sample data
    val testData: StreamRecord[String] = new StreamRecord[String](ObservationData.OBS_FAIL_MISSING_SPATIAL_DATA)
    harness.processElement(testData)

    harness.getSideOutput(obsConfig.failedObsOutputTag) should have size 1
    /***
   --- BEGIN MESSAGES ---
error: object has missing required properties (["spatialExtent"])
    level: "error"
    schema: {"loadingURI":"#","pointer":""}
    instance: {"pointer":""}
    domain: "validation"
    keyword: "required"
    required: ["assetRef","id","integrationAccountRef","obsCode","parentCollectionRef","phenTime","spatialExtent","value","valueUoM"]
    missing: ["spatialExtent"]
---  END MESSAGES  ---
     ****/

    harness.close()
  }

  "ObservationSchemaFunction" should "write data into failed topic due to wrong value data" in {
    // wrap user defined function into a the corresponding operator
    val harness = ProcessFunctionTestHarnesses.forProcessFunction(processFunction)


    //StreamRecord object with the sample data
    val testData: StreamRecord[String] = new StreamRecord[String](ObservationData.OBS_WRONG_VALUE_DATA)
    harness.processElement(testData)

    harness.getSideOutput(obsConfig.failedObsOutputTag) should have size 1
    /***
--- BEGIN MESSAGES ---
error: instance type (string) does not match any allowed primitive type (allowed: ["integer","number"])
    level: "error"
    schema: {"loadingURI":"#","pointer":"/properties/value"}
    instance: {"pointer":"/value"}
    domain: "validation"
    keyword: "type"
    found: "string"
    expected: ["integer","number"]
---  END MESSAGES  ---
     ****/
    harness.close()
  }

  "ObservationSchemaFunction" should "write data into valid topic with aggregate time" in {
    // wrap user defined function into a the corresponding operator
    val harness = ProcessFunctionTestHarnesses.forProcessFunction(processFunction)


    //StreamRecord object with the sample data
    val testData: StreamRecord[String] = new StreamRecord[String](ObservationData.OBS_VALID_AGG_TIME_DATA)
    harness.processElement(testData)

    harness.getSideOutput(obsConfig.validObsOutputTag) should have size 1

    harness.close()
  }

  "ObservationSchemaFunction" should "write data into invalid topic due to wrong aggTimeWindow" in {
    // wrap user defined function into a the corresponding operator
    val harness = ProcessFunctionTestHarnesses.forProcessFunction(processFunction)


    //StreamRecord object with the sample data
    val testData: StreamRecord[String] = new StreamRecord[String](ObservationData.OBS_INVALID_AGG_TIME_DATA)
    harness.processElement(testData)

    harness.getSideOutput(obsConfig.failedObsOutputTag) should have size 1
    /***
--- BEGIN MESSAGES ---
error: object has missing required properties (["selector"])
    level: "error"
    schema: {"loadingURI":"#","pointer":"/properties/aggTimeWindow"}
    instance: {"pointer":"/aggTimeWindow"}
    domain: "validation"
    keyword: "required"
    required: ["componentCode","selector","value","valueUoM"]
    missing: ["selector"]
---  END MESSAGES  ---
     ****/
    harness.close()
  }
}
