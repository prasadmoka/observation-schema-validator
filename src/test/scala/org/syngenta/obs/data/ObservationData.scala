package org.syngenta.obs.data

object ObservationData {

  val OBS_TEST_DATA: String =
    """{"id":"abcd1234","parentCollectionRef":["abcd","1234"],
      |"integrationAccountRef":"intAccount","assetRef":"macBook","obsCode":"obs1",
      |"phenTime":"2021-11-01 11:15AM","valueUoM":"hr","value":1,
      |"spatialExtent":{"type":"location","latCoordinates":[-26.456780],"lonCoordinates":[89.987654]}
      |}""".stripMargin

  val OBS_FAIL_SPATIAL_TYPE_DATA: String =
    """{"id":"abcd1234","parentCollectionRef":["abcd","1234"],
      |"integrationAccountRef":"intAccount","assetRef":"macBook","obsCode":"obs1",
      |"phenTime":"2021-11-01 11:15AM","valueUoM":"hr","value":1,
      |"spatialExtent":{"latCoordinates":[-26.456780],"lonCoordinates":[89.987654]}
      |}""".stripMargin

  val OBS_FAIL_MISSING_SPATIAL_DATA: String =
    """{"id":"abcd1234","parentCollectionRef":["abcd","1234"],
      |"integrationAccountRef":"intAccount","assetRef":"macBook","obsCode":"obs1",
      |"phenTime":"2021-11-01 11:15AM","valueUoM":"hr","value":1}
      |}""".stripMargin

  val OBS_WRONG_VALUE_DATA: String =
    """{"id":"abcd1234","parentCollectionRef":["abcd","1234"],
      |"integrationAccountRef":"intAccount","assetRef":"macBook","obsCode":"obs1",
      |"phenTime":"2021-11-01 11:15AM","valueUoM":"hr","value":"1",
      |"spatialExtent":{"type":"location","latCoordinates":[-26.456780],"lonCoordinates":[89.987654]}
      |}""".stripMargin

  val OBS_VALID_AGG_TIME_DATA: String =
    """{"id":"abcd1234","parentCollectionRef":["abcd","1234"],
      |"integrationAccountRef":"intAccount","assetRef":"macBook","obsCode":"obs1",
      |"phenTime":"2021-11-01 11:15AM","valueUoM":"hr","value":1,
      |"spatialExtent":{"type":"location","latCoordinates":[-26.456780],"lonCoordinates":[89.987654]},
      |"aggTimeWindow":{"componentCode":"CC_AGG_TIME_HOUR","value":2,"valueUoM":"hr","selector":"DURATION"}
      |}""".stripMargin

  val OBS_INVALID_AGG_TIME_DATA: String =
    """{"id":"abcd1234","parentCollectionRef":["abcd","1234"],
      |"integrationAccountRef":"intAccount","assetRef":"macBook","obsCode":"obs1",
      |"phenTime":"2021-11-01 11:15AM","valueUoM":"hr","value":1,
      |"spatialExtent":{"type":"location","latCoordinates":[-26.456780],"lonCoordinates":[89.987654]},
      |"aggTimeWindow":{"componentCode":"CC_AGG_TIME_HOUR","value":2,"valueUoM":"hr"}
      |}""".stripMargin
}
