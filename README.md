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
                     String userToken = Client.getUserToken("deathstar", "darthvader", "d@Rk$1De", null, null);
                     System.out.println(userToken);
              } catch (IOException | SendSecureException e) {
                     throw e;
              }
       }
}
```

## SafeBox Creation

Here is the minimum required code to create a SafeBox – with 1 recipient, a subject, a message and 1 attachment.
This example uses the enterprise account's *default* security profile (which requires to be set in the account).

### With SafeBox Helper Class

```java
import com.xmedius.sendsecure.*;
import com.xmedius.sendsecure.helper.*;

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

<!-- ### Without SafeBox Helper Class

```java
TBD
```
 -->
<a name="usage"></a>
# Usage

## Helper Methods

### Get User Token
```
getUserToken(String enterpriseAccount, String username, String password, String otp, String endpoint)
```
Gets an API Token for a specific user within a SendSecure enterprise account.

Param             | Definition
------------------|-----------
enterpriseAccount | The SendSecure enterprise account
username          | The username of a SendSecure user of the current enterprise account
password          | The password of this user
otp               | The one-time password of this user (if any)
endpoint          | The URL to the SendSecure service ("https://portal.xmedius.com" will be used by default if empty)

Returns the API Token to be used for the specified user.

### Client Object Constructor
```
Client(String apiToken, String enterpriseAccount, String endpoint, String locale)
```

Param             | Definition
------------------|-----------
apiToken          | The API Token to be used for authentication with the SendSecure service
enterpriseAccount | The SendSecure enterprise account
endpoint          | The URL to the SendSecure service ("https://portal.xmedius.com" will be used by default if empty)
locale            | The locale in which the server errors will be returned ("en" will be used by default if empty)

### Submit SafeBox
```
submitSafebox(Safebox safebox)
```
This method is a high-level combo that initializes the SafeBox, uploads all attachments and commits the SafeBox.

Param      | Definition
-----------|-----------
safebox    | A non-initialized Safebox object with security profile, recipient(s), subject, message and attachments (not yet uploaded) already defined. 


### Get Default Security Profile
```
getDefaultSecurityProfile(String userEmail)
```
Retrieves the default security profile of the enterprise account for a specific user.
A default security profile must have been set in the enterprise account, otherwise the method will fail.

Param      | Definition
-----------|-----------
userEmail  | The email address of a SendSecure user of the current enterprise account

Returns the default security profile of the enterprise, with all its setting values/properties.

### Initialize SafeBox
```
initializeSafebox(Safebox safebox)
```
Pre-creates a SafeBox on the SendSecure system and initializes the Safebox object accordingly.

Param      | Definition
-----------|-----------
safebox    | A Safebox object to be initialized by the SendSecure system

Returns the updated SafeBox object with the necessary system parameters (GUID, public encryption key, upload URL) filled out.

### Upload Attachment
```
uploadAttachment(Safebox safebox, Attachment attachment)
```
Uploads the specified file as an Attachment of the specified SafeBox.

Param      | Definition
-----------|-----------
safebox    | An initialized Safebox object
attachment | An Attachment object - the file to upload to the SendSecure system

Returns the updated Attachment object with the GUID parameter filled out.

### Commit SafeBox
```
commitSafebox(Safebox safebox)
```
Finalizes the creation (commit) of the SafeBox on the SendSecure system.
This actually "Sends" the SafeBox with all content and contact info previously specified.

Param      | Definition
-----------|-----------
safebox    | A Safebox object already initialized, with security profile, recipient(s), subject and message already defined, and attachments already uploaded. 

### Get Security Profiles
```
getSecurityProfiles(String userEmail)
```
Retrieves all available security profiles of the enterprise account for a specific user.

Param      | Definition
-----------|-----------
userEmail  | The email address of a SendSecure user of the current enterprise account

Returns the list of all security profiles of the enterprise account, with all their setting values/properties.

### Get Enterprise Settings
```
enterprise_settings
```
Returns all values/properties of the enterprise account's settings specific to SendSecure.

### Helper Modules

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
