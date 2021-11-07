package org.syngenta.obs.functions

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jackson.JsonLoader
import com.github.fge.jsonschema.main.JsonSchemaFactory
import org.syngenta.obs.data.ObservationData

object JsonValidationExample {

  val schemaJsonString = """
{
    "$schema": "http://json-schema.org/draft-04/schema#",
    "title": "Product",
    "description": "A product from the catalog",
    "type": "object",
    "properties": {
        "id": {
            "description": "The unique identifier for a product",
            "type": "integer"
        },
        "name": {
            "description": "Name of the product",
            "type": "string"
        },
        "price": {
            "type": "number",
            "minimum": 0,
            "exclusiveMinimum": true
        },
        "dimensions": {
      "type": "object",
      "properties": {
        "length": {
          "type": "number"
        },
        "width": {
          "type": "number"
        },
        "height": {
          "type": "number"
        }
      },
      "required": [ "length", "width", "height" ]
    }
    },
    "required": ["id", "name", "price"]
}
"""
  val sampleJsonString = """
{
    "id": 1,
    "name": "1124124",
    "price": 5,
    "dimensions": {
      "length": 7.0,
      "width": 12.0,
      "height": 9.5
    }
}
"""

  val observationSchema =
    """
      |{
      |  "$schema": "https://json-schema.org/draft/2020-12/schema",
      |  "id": "https://syngenta.com/obervation.schema.json",
      |  "title": "Observations",
      |  "description": "A product in the catalog",
      |  "type": "object",
      |  "properties": {
      |    "id": {
      |      "description": "The unique identifier for an observation",
      |      "type": "string"
      |    },
      |    "parentCollectionRef": {
      |      "description": "The parent observation reference",
      |      "type": "array",
      |      "items": {
      |        "type": "string"
      |      }
      |    },
      |    "integrationAccountRef": {
      |      "description": "The integration reference account for an observation",
      |      "type": "string"
      |    },
      |    "assetRef": {
      |      "description": "The asset reference",
      |      "type": "string"
      |    },
      |    "obsCode": {
      |      "description": "The code for an observation",
      |      "type": "string"
      |    },
      |    "phenTime": {
      |      "description": "The time for an observation",
      |      "type": "string"
      |    },
      |    "valueUoM": {
      |      "description": "The value of an UoM",
      |      "type": "string"
      |    },
      |    "value": {
      |      "description": "The value of the obs",
      |      "type": "number"
      |    },
      |    "spatialExtent": {
      |      "description": "The spatial details for an observation",
      |      "type": "object",
      |      "properties": {
      |        "type": {
      |          "type" : "string"
      |        },
      |        "latCoordinates": {
      |          "type": "array",
      |          "items": {
      |            "type": "number"
      |          }
      |        },
      |        "lonCoordinates": {
      |          "type": "array",
      |          "items": {
      |            "type": "number"
      |          }
      |        }
      |      },
      |      "required": [ "type", "latCoordinates", "lonCoordinates" ]
      |    },
      |    "aggTimeWindow": {
      |      "type": "object",
      |      "properties": {
      |        "componentCode":{
      |          "type": "string"
      |        },
      |        "value": {"type":"number"},
      |        "valueUoM": {"type":"string"},
      |        "selector": {"type":"string"}
      |      },
      |      "required": [ "componentCode", "value", "valueUoM", "selector" ]
      |    },
      |    "aggMethod": {
      |      "type": "object",
      |      "properties": {
      |        "componentCode":{ "type" : "string"},
      |        "selector": {"type":"string"}
      |      },
      |     "required": ["componentCode","selector"]
      |    },
      |    "parameter": {
      |      "type": "object",
      |      "properties": {
      |        "componentCode": {"type":"string"},
      |        "value": {"type":"number"},
      |        "selector": {"type":"string"}
      |      },
      |      "required": ["componentCode", "value","selector"]
      |    },
      |    "featureOfInterest": {
      |      "type": "object",
      |      "properties": {
      |        "componentCode": {"type":"string"},
      |        "selector": {"type":"string"}
      |      },
      |      "required": ["componentCode","selector"]
      |    },
      |    "obsProperty": {
      |      "type": "object",
      |      "properties": {
      |        "componentCode": {"type":"string"},
      |        "selector": {"type":"string"}
      |      },
      |      "required": ["componentCode","selector"]
      |    },
      |    "samplingStrategy": {
      |      "type": "object",
      |      "properties": {
      |        "componentCode": {"type":"string"},
      |        "selector": {"type":"string"}
      |      },
      |      "required": ["componentCode","selector"]
      |    },
      |    "obsMethod": {
      |      "type": "object",
      |      "properties": {
      |        "componentCode": {"type":"string"},
      |        "selector": {"type":"string"}
      |      },
      |      "required": ["componentCode","selector"]
      |    },
      |    "metadata": {
      |      "type": "object",
      |      "properties": {
      |        "componentCode": {"type":"string"},
      |        "selector": {"type":"string"}
      |      },
      |      "required": ["componentCode","selector"]
      |    },
      |    "deviceMetadata": {
      |      "type": "object",
      |      "properties": {
      |        "componentCode": {"type":"string"},
      |        "selector": {"type":"string"}
      |      },
      |      "required": ["componentCode","selector"]
      |    },
      |    "dataQuality": {
      |      "type": "object",
      |      "properties": {
      |        "componentCode": {"type":"string"},
      |        "selector": {"type":"string"}
      |      },
      |      "required": ["componentCode","selector"]
      |    },
      |    "event": {
      |      "type": "object",
      |      "properties": {
      |        "componentCode": {"type":"string"},
      |        "selector": {"type":"string"}
      |      },
      |      "required": ["componentCode","selector"]
      |    }
      |  },
      |  "required": [ "id","parentCollectionRef","integrationAccountRef", "assetRef", "obsCode","phenTime","valueUoM","value","spatialExtent"]
      |}
      |""".stripMargin

  def main(args: Array[String]): Unit = {
    // You can read a schema from String, java.io.InputStream, URI, etc.
    val schema = JsonSchemaFactory.byDefault().getJsonSchema(JsonLoader.fromString(schemaJsonString))
    // You can read a JSON object from String, a file, URL, etc.
    val parsedJson = new ObjectMapper().readTree(sampleJsonString)

    val validationMessages = schema.validate(parsedJson)
    println(validationMessages.isSuccess)
    //validationMessages.forEach(msg => println(msg.getMessage))

    val obsSchema = JsonSchemaFactory.byDefault().getJsonSchema(JsonLoader.fromString(observationSchema))
    val obsParsedJson = new ObjectMapper().readTree(ObservationData.OBS_TEST_DATA)
    val obsValidationMessages = obsSchema.validate(obsParsedJson)
    println(obsValidationMessages.isSuccess)
    obsValidationMessages.forEach(msg => println(msg.getMessage))

  }
}
