@APISuite
Feature: [CLOUD API Experimental Suite] - [E2E test scenario for JVCLD-26254]

  Background:
    Given We are running E2E JVCLD-26254
    And ADMIN logged into the system
    And User USER1 is created
    And User USER2 is created

  Scenario: API - Verify that user is able to send animated gif in the Direct Message [25269973]
    Given User USER1 logs in
    When User send Direct Message "DM1" with animated gif to user "USER2"
      |image                                     |
      |https://dummyimage.com/600x400/000/ff1.gif|
      |https://dummyimage.com/600x400/000/ff2.gif|
    Then Direct message "DM1" sent successfully
    And Direct message has attached images
      |image  |
      |ff1.gif|
      |ff2.gif|

  Scenario: API - Verify that user is able to receive animated gif in the Direct Message [25269974]
    Given User USER1 logs in
    And User send Direct Message "DM2" with animated gif to user "USER2"
      |image                                     |
      |https://dummyimage.com/600x400/000/ff1.gif|
      |https://dummyimage.com/600x400/000/ff2.gif|
    Then Direct message "DM2" sent successfully
    And Direct message has attached images
      |image  |
      |ff1.gif|
      |ff2.gif|
    When User USER2 logs in
    And User request direct message "DM2"
    Then Direct message has attached images
      |image|
      |ff1.gif|
      |ff2.gif|

  Scenario: API - Verify that direct message with JPG image through Inbox can be sent [25269979]
    Given User USER1 logs in
    When User send Direct Message "DM3" with JPG image to user "USER2"
      |image                                     |
      |https://dummyimage.com/600x400/000/ff1.jpg|
    Then Direct message "DM3" sent successfully
    And Direct message has attached images
      |image  |
      |ff1.jpg|

  Scenario: API - Verify that direct message with JPG image through Inbox can be received [25269980]
    Given User USER1 logs in
    And User send Direct Message "DM4" with JPG image to user "USER2"
      |image                                     |
      |https://dummyimage.com/600x400/000/ff1.jpg|
    Then Direct message "DM4" sent successfully
    And Direct message has attached images
      |image  |
      |ff1.jpg|
    When User USER2 logs in
    And User request direct message "DM4"
    Then Direct message has attached images
      |image  |
      |ff1.jpg|
