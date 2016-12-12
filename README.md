**XMediusSENDSECURE (SendSecure)** is a collaborative file exchange platform that is both highly secure and simple to use.
It is expressly designed to allow for the secured exchange of sensitive documents via virtual SafeBoxes.

SendSecure comes with a **Web API**, which is **RESTful**, uses **HTTPs** and returns **JSON**.

Specific libraries have been published for various languages:
[C#](https://github.com/xmedius/sendsecure-csharp),
**Java**,
[JavaScript](https://github.com/xmedius/sendsecure-js),
[PHP](https://github.com/xmedius/sendsecure-php),
[Python](https://github.com/xmedius/sendsecure-python)
and
[Ruby](https://github.com/xmedius/sendsecure-ruby).

# sendsecure-java

**This library allows you to use the SendSecure Web API via Java.**

With this library, you will be able to:
* Authenticate SendSecure users
* Create new SafeBoxes

# Table of Contents

* [Installation](#installation)
* [Quick Start](#quickstart)
* [Usage](#usage)
* [License](#license)
* [Credits](#credits)

<a name="installation"></a>
# Installation

## Prerequisites

- Java version Oracle JDK 8
- The SendSecure service, provided by [XMedius](https://www.xmedius.com/en/products?source=sendsecure-java) (demo accounts available on demand)

## Install Package

### via Maven

```
mvn install
```

<a name="quickstart"></a>
# Quick Start

## Authentication (Retrieving API Token)

Authentication is done using an API Token, which must be first obtained based on SendSecure enterprise account and user credentials.
Here is the minimum code to get such a user-based API Token.

```java
import com.xmedius.sendsecure.Client;
import com.xmedius.sendsecure.exception.SendSecureException;

import java.io.IOException;

public class Example {
       public static void main(String[] args) throws Exception {
              try {
                     String userToken = Client.getUserToken("deathstar", "darthvader", "d@Rk$1De", "DV-TIE/x1", "TIE Advanced x1", "The Force App", "https://portal.xmedius.com", null);
                     System.out.println(userToken);
              } catch (IOException | SendSecureException e) {
                     throw e;
              }
       }
}
```

## SafeBox Creation (Using SafeBox Helper Class)

Here is the minimum required code to create a SafeBox – with 1 recipient, a subject, a message and 1 attachment.
This example uses the user's *default* security profile (which requires to be set in the account).

```java
import com.xmedius.sendsecure.*;
import com.xmedius.sendsecure.helper.*;
import com.xmedius.sendsecure.helper.ContactMethod.DestinationType;

import java.io.File;

public class Example {
       public static void main(String[] args) throws Exception {
              try {
                     String userEmail = "darthvader@empire.com";
                     String token = "USER|1d495165-4953-4457-8b5b-4fcf801e621a";
                     String enterpriseAccount = "deathstar";
                     String endpoint = "https://portal.xmedius.com";

                     Safebox safebox = new Safebox(userEmail);
                     safebox.setSubject("Family matters");
                     safebox.setMessage("Son, you will find attached the evidence.");

                     Recipient recipient = new Recipient("lukeskywalker@rebels.com");
                     ContactMethod contactMethod = new ContactMethod("555-232-5334", DestinationType.CELL_PHONE);
                     recipient.getContactMethods().add(contactMethod);
                     safebox.getRecipients().add(recipient);

                     Attachment attachment = new Attachment(new File("Birth_Certificate.pdf"), "application/pdf");
                     safebox.getAttachments().add(attachment);

                     Client client = new Client(token, enterpriseAccount, endpoint);
                     SafeboxResponse response = client.submitSafebox(safebox);
              } catch (Exception e) {
                     e.printStackTrace();
              }
       }
}
```

<a name="usage"></a>
# Usage

## Helper Methods

### Get User Token
```
getUserToken(enterpriseAccount, username, password, deviceId, deviceName, applicationType, endpoint, oneTimePassword)
```
Creates and returns an API Token for a specific user within a SendSecure enterprise account.
Calling this method again with the exact same params will always return the same Token.

Param             | Type   | Definition
------------------|--------|-----------
enterpriseAccount | String | The SendSecure enterprise account
username          | String | The username of a SendSecure user of the current enterprise account
password          | String | The password of this user
deviceId          | String | The unique ID of the device used to get the Token 
deviceName        | String | The name of the device used to get the Token
applicationType   | String | The type/name of the application used to get the Token ("SendSecure Java" will be used by default if empty)
endpoint          | String | The URL to the SendSecure service ("https://portal.xmedius.com" will be used by default if empty)
oneTimePassword   | String | The one-time password of this user (if any)

### Client Object Constructor
```
Client(apiToken, enterpriseAccount, endpoint, locale)
```

Param             | Type   | Definition
------------------|--------|-----------
apiToken          | String | The API Token to be used for authentication with the SendSecure service
enterpriseAccount | String | The SendSecure enterprise account
endpoint          | String | The URL to the SendSecure service ("https://portal.xmedius.com" will be used by default if empty)
locale            | String | The locale in which the server errors will be returned ("en" will be used by default if empty)

### Get Enterprise Settings
```
getEnterpriseSettings()
```
Returns all values/properties of the enterprise account's settings specific to SendSecure.

### Get Default Security Profile
```
getDefaultSecurityProfile(userEmail)
```
Returns the default security profile (if it has been set) for a specific user, with all its setting values/properties.

Param      | Type   | Definition
-----------|--------|-----------
userEmail  | String | The email address of a SendSecure user of the current enterprise account

### Get Security Profiles
```
getSecurityProfiles(userEmail)
```
Returns the list of all security profiles available to a specific user, with all their setting values/properties.

Param      | Type   | Definition
-----------|--------|-----------
userEmail  | String | The email address of a SendSecure user of the current enterprise account

### Initialize SafeBox
```
initializeSafebox(safebox)
```
Pre-creates a SafeBox on the SendSecure system and returns the updated Safebox object with the necessary system parameters filled out (GUID, public encryption key, upload URL).

Param      | Type    | Definition
-----------|---------|-----------
safebox    | Safebox | A Safebox object to be initialized by the SendSecure system

### Upload Attachment
```
uploadAttachment(safebox, attachment)
```
Uploads the specified file as an Attachment of the specified SafeBox and returns the updated Attachment object with the GUID parameter filled out.

Param      | Type       | Definition
-----------|------------|-----------
safebox    | Safebox    | An initialized Safebox object
attachment | Attachment | An Attachment object - the file to upload to the SendSecure system

### Commit SafeBox
```
commitSafebox(safebox)
```
Finalizes the creation (commit) of the SafeBox on the SendSecure system.
This actually "Sends" the SafeBox with all content and contact info previously specified.

Param      | Type    | Definition
-----------|---------|-----------
safebox    | Safebox | A Safebox object already initialized, with security profile, recipient(s), subject and message already defined, and attachments already uploaded. 

### Submit SafeBox
```
submitSafebox(safebox)
```
This method is a high-level combo that initializes the SafeBox, uploads all attachments and commits the SafeBox.

Param      | Type    | Definition
-----------|---------|-----------
safebox    | Safebox | A non-initialized Safebox object with security profile, recipient(s), subject, message and attachments (not yet uploaded) already defined. 


## Helper Modules

### Safebox

### SafeboxResponse

### Attachment

### Recipient

### ContactMethod

### SecurityProfile

### EnterpriseSettings

### ExtensionFilter

<a name="license"></a>
# License

sendsecure-java is distributed under [MIT License](https://github.com/xmedius/sendsecure-java/blob/master/LICENSE).

<a name="credits"></a>
# Credits

sendsecure-java is developed, maintained and supported by [XMedius Solutions Inc.](https://www.xmedius.com?source=sendsecure-java)
The names and logos for sendsecure-java are trademarks of XMedius Solutions Inc.

![XMedius Logo](https://s3.amazonaws.com/xmc-public/images/xmedius-site-logo.png)
