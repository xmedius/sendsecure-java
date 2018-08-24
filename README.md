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

# Installation

## Prerequisites

- Java version Oracle JDK 8
- The SendSecure service, provided by [XMedius](https://www.xmedius.com/en/products?source=sendsecure-java) (demo accounts available on demand)

## Install Package

### via Maven

```
mvn install
```

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
                     UserToken tokenDetail = Client.getUserToken("deathstar", "darthvader", "d@Rk$1De", "DV-TIE/x1", "TIE Advanced x1", "The Force App", "https://portal.xmedius.com", null);
                     System.out.println(tokenDetail.getToken());
                     System.out.println(tokenDetail.getUserId());
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

                     Participant recipient = new Participant("lukeskywalker@rebels.com");
                     ContactMethod contactMethod = new ContactMethod("555-232-5334", DestinationType.CELL_PHONE);
                     recipient.addContactMethod(contactMethod);
                     safebox.getParticipants().add(recipient);

                     Attachment attachment = new Attachment(new File("Birth_Certificate.pdf"), "application/pdf");
                     safebox.getAttachments().add(attachment);

                     Client client = new Client(token, enterpriseAccount, endpoint);
                     Safebox response = client.submitSafebox(safebox);
              } catch (Exception e) {
                     e.printStackTrace();
              }
       }
}
```

# Usage

## Helper Methods

### Get User Token
```
getUserToken(enterpriseAccount, username, password, deviceId, deviceName, applicationType, endpoint, oneTimePassword)
```
Creates and returns two properties: an API Token and a user ID, for a specific user within a SendSecure enterprise account.
Calling this method again with the exact same params will always return the same Token.

Param             | Definition
------------------|-----------
enterpriseAccount | The SendSecure enterprise account
username          | The username of a SendSecure user of the current enterprise account
password          | The password of this user
deviceId          | The unique ID of the device used to get the Token
deviceName        | The name of the device used to get the Token
applicationType   | The type/name of the application used to get the Token ("SendSecure Java" will be used by default if empty)
endpoint          | The URL to the SendSecure service ("https://portal.xmedius.com" will be used by default if empty)
oneTimePassword   | The one-time password of this user (if any)

### Client Object Constructor
```
Client(apiToken, enterpriseAccount, endpoint, locale)
Client(apiToken, userId, enterpriseAccount, endpoint, locale)
```

Param             | Definition
------------------|-----------
apiToken          | The API Token to be used for authentication with the SendSecure service
enterpriseAccount | The SendSecure enterprise account
endpoint          | The URL to the SendSecure service ("https://portal.xmedius.com" will be used by default if empty)
locale            | The locale in which the server errors will be returned ("en" will be used by default if empty)
userId            | The user ID, which may be used to manage additional objects directly related to the user (e.g. favorites)

### Enterprise Methods

### Get Enterprise Settings
```
getEnterpriseSettings()
```
Returns all SendSecure General Settings values/properties of the current enterprise account.

### Get Default Security Profile
```
getDefaultSecurityProfile(userEmail)
```
Returns the default security profile (if it has been set) for a specific user, with all its setting values/properties.

Param      | Definition
-----------|-----------
userEmail  | The email address of a SendSecure user of the current enterprise account

### Get Security Profiles
```
getSecurityProfiles(userEmail)
```
Returns the list of all security profiles available to a specific user, with all their setting values/properties.

Param      | Definition
-----------|-----------
userEmail  | The email address of a SendSecure user of the current enterprise account

### Consent Message Group Methods

#### Get Consent Message (in all locales)
```
getConsentGroupMessages(int consentGroupId)
```
Retrieves the consent message (in all available locales) associated to a Security Profile or a SafeBox, among the available consent messages of the current enterprise account.

Param               | Definition
--------------------|-----------
consentGroupId      | The unique ID of the consent group.


### SafeBox Creation Methods

#### Initialize SafeBox
```
initializeSafebox(Safebox safebox)
```
Pre-creates a SafeBox on the SendSecure system and returns the updated [Safebox](#safebox) object with the necessary system parameters filled out (GUID, public encryption key, upload URL).

Param      | Definition
-----------|-----------
safebox    | A [Safebox](#safebox) object to be initialized by the SendSecure system



### Initialize SafeBox
```
initializeSafebox(Safebox safebox)
```
Pre-creates a SafeBox on the SendSecure system and returns the updated [Safebox](#safebox) object with the necessary system parameters filled out (GUID, public encryption key, upload URL).

Param      | Definition
-----------|-----------
safebox    | A [Safebox](#safebox) object to be initialized by the SendSecure system

### Upload Attachment
```
uploadAttachment(Safebox safebox, Attachment attachment)
```
Uploads the specified file as an Attachment of the specified SafeBox and returns the updated [Attachment](#attachment) object with the GUID parameter filled out.

Param      | Definition
-----------|-----------
safebox    | An initialized [Safebox](#safebox) object
attachment | An [Attachment](#attachment) object - the file to upload to the SendSecure system

### Commit SafeBox
```
commitSafebox(Safebox safebox)
```
Finalizes the creation (commit) of the SafeBox on the SendSecure system.
This actually "Sends" the SafeBox with all content and contact info previously specified.

Param      | Definition
-----------|-----------
safebox    | A [Safebox](#safebox) object already initialized, with security profile, recipient(s), subject and message already defined, and attachments already uploaded.

### Submit SafeBox
```
submitSafebox(Safebox safebox)
```
This high-level method combines the SafeBox initialization, attachment uploads and the SafeBox commit.

Param      | Definition
-----------|-----------
safebox    | A non-initialized [Safebox](#safebox) object with security profile, recipient(s), subject, message and attachments (not yet uploaded) already defined.

### Safebox Methods

#### Reply
```
reply(Safebox safebox, Reply reply)
```
Replies to a specific SafeBox with the content specified through a Reply object.

Param                | Definition
---------------------|-----------
safebox              | A [Safebox](#safebox) object.
reply                | A [Reply](#reply-object) object.

#### Add Time
```
addTime(Safebox safebox, int value, SecurityEnums.TimeUnit unit)
```
Extends the SafeBox duration by the specified amount of time.

Param                | Definition
---------------------|-----------
safebox              | A [Safebox](#safebox) object.
value                | The time value, according to the specified unit.
unit                 | The time unit. Accepted values:```hours```, ```days```, ```weeks```, ```months```.

#### Close SafeBox
```
closeSafebox(Safebox safebox)
```
Closes the SafeBox immediately, i.e. before its intended expiration. Only available for SafeBoxes in "open" status.

Param                | Definition
---------------------|-----------
safebox              | A [Safebox](#safebox) object.

#### Delete SafeBox Content
```
deleteSafeboxContent(Safebox safebox)
```
Deletes the SafeBox content immediately, i.e. despite the remaining retention period. Only available for SafeBoxes in "closed" status.

Param                | Definition
---------------------|-----------
safebox              | A [Safebox](#safebox) object.

#### Mark SafeBox as Read
```
markAsRead(Safebox safebox)
```
Marks as read all messages within the SafeBox.

Param                | Definition
---------------------|-----------
safebox              | A [Safebox](#safebox) object.

#### Mark SafeBox as Unread
```
markAsUnread(Safebox safebox)
```
Marks as unread all messages within the SafeBox.

Param                | Definition
---------------------|-----------
safebox              | A [Safebox](#safebox) object.

#### Mark Message as Read
```
markAsReadMessage(Safebox safebox, Message message)
```
Marks as read a specific message within the SafeBox.

Param                | Definition
---------------------|-----------
safebox              | A [Safebox](#safebox) object.
message              | A [Message](#message) object.

#### Mark Message as Unread
```
markAsUnreadMessage(Safebox safebox, Message message)
```
Marks as unread a specific message within the SafeBox.

Param                | Definition
---------------------|-----------
safebox              | A [Safebox](#safebox) object.
message              | A [Message](#message) object.

#### Get Audit Record PDF
```
getAuditRecordPdf(Safebox safebox)
```
Gets the Audit Record of the SafeBox.

Param                | Definition
---------------------|-----------
safebox              | A [Safebox](#safebox) object.

#### Get Audit Record PDF URL
```
getAuditRecordPdfUrl(Safebox safebox)
```
Gets the URL of the Audit Record of the SafeBox.

Param                | Definition
---------------------|-----------
safebox              | A [Safebox](#safebox) object.

#### Get SafeBox Info
```
getSafeboxInfo(Safebox safebox, List<String> sections)
```
Gets all information of the SafeBox, regrouped by sections.

Param                | Definition
---------------------|-----------
safebox              | A [Safebox](#safebox) object.
sections             | The information sections to be retrieved. Accepted values: ```download_activity```, ```event_history```, ```messages```, ```participants```, ```security_options```.

#### Get SafeBox Participants
```
getSafeboxParticipants(Safebox safebox)
```
Gets the list of all participants of the SafeBox.

Param                | Definition
---------------------|-----------
safebox              | A [Safebox](#safebox) object.

#### Get SafeBox Messages
```
getSafeboxMessages(Safebox safebox)
```
Gets all the messages of the SafeBox.

Param                | Definition
---------------------|-----------
safebox              | A [Safebox](#safebox) object.

#### Get SafeBox Security Options
```
getSafeboxSecurityOptions(Safebox safebox)
```
Gets all the security options of the SafeBox.

Param                | Definition
---------------------|-----------
safebox              | A [Safebox](#safebox) object.

#### Get SafeBox Download Activity
```
getSafeboxDownloadActivity(Safebox safebox)
```
Gets all the download activity information of the SafeBox.

Param                | Definition
---------------------|-----------
safebox              | A [Safebox](#safebox) object.

#### Get SafeBox Event History
```
getSafeboxEventHistory(Safebox safebox)
```
Retrieves the complete event history of the SafeBox.

Param                | Definition
---------------------|-----------
safebox              | A [Safebox](#safebox) object.

### SafeBox Attached Document Methods

#### Get File URL
```
getFileUrl(Safebox safebox, Attachment document)
```
Returns the URL of a document contained in a SafeBox (allowing to download it from the File Server).

Param      | Definition
-----------|-----------
safebox    | A [Safebox](#safebox) object.
document   | An [Attachment](#attachment) object.

### Participant Management Methods

#### Create Participant
```
createParticipant(Participant participant, Safebox safebox)
```
Creates a new Participant and adds it to the SafeBox.

Param        | Definition
-------------|-------------
safebox      | A [Safebox](#safebox) object.
participant  | A [Participant](#participant) object.

#### Update Participant
```
updateParticipant(Participant participant, Safebox safebox)
```
Updates an existing participant of the specified SafeBox.

Param        | Definition
-------------|-------------
safebox      | A [Safebox](#safebox) object.
participant  | The updated [Participant](#participant) object.

#### Delete Participant's Contact Methods
```
deleteParticipantContactMethods(Participant participant, Safebox safebox, List<Integer> contactMethodIds)
```
Deletes one or several contact methods of an existing participant of the specified SafeBox.

Param                | Definition
---------------------|---------------------
safebox              | A [Safebox](#safebox) object.
participant          | A [Participant](#participant) object.
contactMethodIds     | A list of contact method unique IDs (see [ContactMethod](#contactmethod) object).

### Recipient Methods

#### Search Recipient
```
searchRecipient(String term)
```
Returns a list of people (e.g. among Favorites and/or Enterprise Users) that match the specified search term, typically for auto-suggestion when adding participants to a new/existing SafeBox. The returned list depends on the *autocomplete* attributes of the Enterprise Settings.

Param      | Definition
-----------|-----------
term       | A string intended to match a portion of name, email address or company.

### SafeBox List (SafeBoxes) Methods

#### Get SafeBox List
```
getSafeboxes(String searchTerm, int page, int safeboxesPerPage, Safebox.Status status)
getSafeboxes(String searchUrl)
getSafeboxes()
```
Returns a SafeboxesResponse object containing the count of found SafeBoxes, the previous page URL, the next page URL and a list of [Safebox](#safebox) objects – for the current user account and according to the specified filtering options.

Param            | Definition
-----------------|-----------
url              | The search url (optional).
searchTerm       | Intended to match a portion of SafeBox subject/ID/message, participant email/first name/last name, attached file name/fingerprint.
page             | Selects the page to return.
safeboxesPerPage | Splits the list in several pages, with 0 < per_page <= 1000 (default is 100).
status           | Filters by SafeBox status: ```in_progress```, ```closed```, ```content_deleted```, ```unread```.
searchUrl        | The complete search URL of a Safebox.

Example to return the 1st page with 20 SafeBoxes that are open and unread and that contain the word "Luke":
```
getSafeboxes("Luke", 1, 20, "in_progress,unread")
```

### User Methods

#### Get User Settings
```
getUserSettings()
```
Retrieves all the SendSecure User Settings for the current user account.

#### Get Favorites
```
getFavorites()
```
Retrieves all favorites associated to the current user account.

#### Create Favorite
```
createFavorite(Favorite favorite)
```
Creates a new favorite for the current user account.

Param                | Definition
---------------------|-----------
favorite             | A [Favorite](#favorite) object.

#### Update Favorite
```
updateFavorite(Favorite favorite)
```
Updates an existing favorite associated to the current user account.

Param                | Definition
---------------------|-----------
favorite             | The updated [Favorite](#favorite) object.

#### Delete Favorite's Contact Methods
```
deleteFavoriteContactMethods(Favorite favorite, List<Integer> contactMethodIds)
```
Deletes one or several contact methods of an existing favorite associated to the current user account.

Param                | Definition
---------------------|-----------
favorite             | A [Favorite](#favorite) object.
contactMethodIds     | A list of contact method unique IDs (see [ContactMethod](#contactmethod) object).

#### Delete Favorite
```
deleteFavorite(int favoriteId)
```
Delete an existing favorite associated to the current user account.

Param                | Definition
---------------------|-----------
favoriteId           | The id of the favorite to be deleted.

## Helper Objects
Here is the alphabetical list of all available objects, with their attributes.

### Attachment

Builds an object to be uploaded to the server as attachment of the SafeBox.
Can be created either with a [File](#file) or a [Stream](#stream).
All attributes are mandatory.

#### File

Attribute            | Definition
---------------------|-----------
guid                 | The unique identifier of the attachment (filled by the system once the file is uploaded).
contentType          | The file Content-type (MIME).
file                 | The file object to upload.

#### Stream

Attribute            | Definition
---------------------|-----------
guid                 | The unique identifier of the attachment (filled by the system once the file is uploaded).
contentType          | The file Content-type (MIME).
stream               | The data to upload.
filename             | The file name.
size                 | The file size.

### ConsentMessage
Builds an object to retrieve a consent message in a specific locale.
Subset of [ConsentMessageGroup](#consentmessagegroup) (regrouping all locales of a same consent message).
All attributes are read only.

Attribute            | Definition
---------------------|-----------
locale               | The locale in which the consent message will be returned.
value                | The text of the consent message.
createdAt            | The creation date of the consent message.
updatedAt            | The last modification date of the consent message.

### ConsentMessageGroup
Builds an object to retrieve all localized versions of the same consent message.
All attributes are read only.

Attribute            | Definition
---------------------|-----------
id                   | The unique ID of the consent message group.
name                 | The name of the consent message group.
createdAt            | The creation date of the consent message group.
updatedAt            | The last modification date of the consent message group.
consentMessages      | The list of [ConsentMessage](#consentMessage) objects (one per available locale).

### ContactMethod
Builds an object to create a phone number destination owned by a participant or a favorite (or retrieve the contact method information).
May be a subset of [GuestOptions](#GuestOptions) or [Favorite](#Favorite).
Any ContactMethod – plus the email address – will be usable as Security Code delivery means to the participant.

Attribute            | Definition
---------------------|-----------
destination          | (mandatory) A phone number owned by the participant.
destinationType      | (mandatory) The phone number's type (i.e. home/cell/office/other).
_destroy             | (contextual\*) Indicates whether the contact method should be deleted or not.
id                   | (read only) The unique ID of the contact method.
verified             | (read only) Indicates whether the contact method was verified by the SendSecure system or not (through authentication mechanism).
createdAt            | (read only) The creation date of the contact method.
updatedAt            | (read only) The last modification date of the contact method.

\* Mandatory only if the existing contact method must be destroyed

### DownloadActivity
Builds an object with all download activity information of all participants of an existing SafeBox.
Subset of [Safebox](#Safebox) object.
All attributes are read only.

Attribute            | Definition
---------------------|-----------
guests               | The list of [DownloadActivityDetail](#downloadActivityDetail) objects associated with each SafeBox participant other than the Owner.
owner                | The [DownloadActivityDetail](#downloadActivityDetail) object associated with the SafeBox Owner.

### DownloadActivityDetail
Builds an object with all the download activity details for a specific participant of the SafeBox.
Subset of [DownloadActivity](#DownloadActivity).
All attributes are read only.

Attribute            | Definition
---------------------|-----------
id                   | The unique ID of the download activity detail.
documents            | The list of [DownloadActivityDocument](#downloadActivityDocument) objects associated with the SafeBox participant.

### DownloadActivityDocument
Builds an object with all the download activity informations for a specific document regarding a specific participant of the SafeBox.
Subset of [DownloadActivityDetail](#downloadActivityDetail).
All attributes are read only.

Attribute            | Definition
---------------------|-----------
id                   | The unique ID of the download activity document.
downloadedBytes      | The number of bytes of the document that were actually downloaded.
downloadedDate       | The date of the download.

### EnterpriseSettings
Builds an object with the SendSecure settings of an Enterprise Account.
All attributes are read only.

Attribute                         | Definition
----------------------------------|--------------------
defaultSecurityProfileId          | The unique ID of the default security profile of the enterprise.
pdfLanguage                       | The language in which all SafeBox Audit Records are generated.
usePdfaAuditRecords               | Indicates whether the Audit Records are generated as PDF/A or not.
internationalDialingPlan          | The country/dialing plan used for formatting national numbers when sending information by phone/SMS.
extensionFilter                   | The [ExtensionFilter](#extensionfilter) object associated with the enterprise account.
virusScanEnabled                  | Indicates whether the virus scan is applied or not when uploading files to SafeBoxes.
maxFileSizeValue                  | The maximum file size allowed for a SafeBox attachment.
maxFileSizeUnit                   | The unit of the maximum file size value.
includeUsersInAutocomplete        | Indicates whether the users of the enterprise account should be included or not in recipient automatic suggestion.
includeFavoritesInAutocomplete    | Indicates whether the favorites of the current user should be included or not in recipient automatic suggestion.
usersPublicUrl                    | Indicates whether the Personal Secure Links are allowed or not for the users of the enterprise account.
createdAt                         | The creation date of the enterprise settings.
updatedAt                         | The last modification date of the enterprise settings.

### EventHistory
Builds an object with all Event History information of a SafeBox.
Subset of [Safebox](#Safebox) object.
All attributes are read only.

Attribute            | Definition
---------------------|-----------
type                 | The type of the event.
date                 | The date of the event.
metadata             | An object containing all available metadata according to the type of event.
message              | The complete message describing the event, localized according to the current user locale.

### ExtensionFilter
Builds an object with the list of allowed or forbidden extensions for SafeBox attachments.
Subset of [EnterpriseSettings](#EnterpriseSettings).
All attributes are read only.

Attribute            | Definition
---------------------|-----------
mode                 | Indicates whether the attachments extensions are allowed or forbidden.
list                 | The list of allowed/forbidden extensions for SafeBox attachments.

### Favorite
Builds an object to create a favorite for a user (or retrieve favorite information).

Attribute            | Definition
---------------------|-----------
email                | (mandatory) The email address of the favorite.
firstName            | (optional) The first name of the favorite.
lastName             | (optional) The last name of the favorite.
companyName          | (optional) The company name of the favorite.
contactMethods       | (contextual\*) The list of all [ContactMethod](#contactmethod) objects of the favorite.
orderNumber          | (optional) The ordering number of the favorite among the other favorites.
id                   | (read only) The unique ID of the favorite.
createdAt            | (read only) The creation date of the favorite.
updatedAt            | (read only) The last modification date of the favorite.

\* May be mandatory in the case the favorite is added as participant to a SafeBox requiring other contact methods than email.

### GuestOptions
Builds an object to create a subset of additional attributes for the Participant (or retrieve participant information).
Subset of [Participant](#Participant).

Attribute             | Definition
----------------------|-----------
companyName           | (optional) The company name of the participant.
locked                | (optional) Indicates whether the participant access to the SafeBox was revoked or not.
contactMethods        | (contextual\*) The list of all [ContactMethod](#contactmethod) objects of the participant.
bouncedEmail          | (read only) Indicates if a NDR was received by the system after sending the invitation email to the participant.
failedLoginAttempts   | (read only) The count of the participant failed login attempts.
verified              | (read only) Indicated whether the participant email address was verified by the SendSecure system or not (through authentication mechanism).
createdAt             | (read only) The creation date of the GuestOptions.
updatedAt             | (read only) The last modification date of the GuestOptions.

\* May be mandatory depending on the Security Profile of the SafeBox.

### Message
Builds an object to retrieve a specific message from an existing SafeBox.
Subset of [Safebox](#Safebox) object.
All attributes are read only.

Attribute            | Definition
---------------------|-----------
id                   | The unique ID of the message.
note                 | The text of the message.
noteSize             | The size (character count) of the message.
read                 | Indicates whether the message was read or not.
authorId             | The unique ID of the message author.
authorType           | The participant type (Owner or other) of the author of the message, regarding the SafeBox.
createdAt            | The creation date of the message.
documents            | The list of all [MessageDocument](#MessageDocument) objects representing the attachments of the message.

### MessageDocument
Builds an object to retrieve all information of a specific document (file) from a message within an existing SafeBox.
Subset of [Message](#Message) object.
All attributes are read only.

Attribute            | Definition
---------------------|-----------
id                   | The unique ID of the file.
name                 | The file name.
sha                  | The fingerprint (SHA-256) of the file.
size                 | The file size.
url                  | The URL of the file.

### Participant
Builds an object to create a participant for the SafeBox (or retrieve participant information).
Subset of [Safebox](#Safebox) object.

Attribute            | Definition
---------------------|-----------
email                | (mandatory) The email address of the participant.
firstName            | (optional) The first name of the participant.
lastName             | (optional) The last name of the participant.
guestOptions         | (optional) The [GuestOptions](#guestoptions) object defining the additional attributes for the participant.
id                   | (read only) The unique ID of the participant.
type                 | (read only) The type of the participant (Owner or other) in the SafeBox.
role                 | (read only) The role of the participant (in terms of permissions) in the SafeBox.
messageReadCount     | (read only) The count of read messages of the participant.
messageTotalCount    | (read only) The total count of messages of the participant.

### PersonalSecureLink
Builds an object to retrieve information about the Personal Secure Link of the current user.
Subset of [UserSettings](#UserSettings).
All attributes are read only.

Attribute              | Definition
-----------------------|-----------
enabled                | Indicates whether the Personal Secure Link of the user is enabled or not.
url                    | The URL of the Personal Secure Link of the user.
securityProfileId      | The ID of the Security Profile used by the Secure Link.


### Reply object
Builds an object to create and post a reply within the SafeBox.

Attribute            | Definition
---------------------|-----------
message              | (contextual\*) The message of the Reply.
attachments          | (contextual\*) The list of all [Attachment](#attachment) objects of the Reply.
consent              | (contextual\*\*) The consent acceptance flag.
documentIds          | (auto-filled) The list of the attachment IDs.

\* A message is mandatory if no attachments are provided, and at least one attachment is required if no message is provided.
\*\* Consent acceptance may be mandatory depending on the Security Profile of the SafeBox.

### Safebox
Builds an object to create a new SafeBox or get all information of an existing SafeBox.
Once the SafeBox is created, all attributes are no longer editable.

Attribute                  | Definition
---------------------------|-----------
guid                       | (auto-filled) The unique ID of the SafeBox (available once the SafeBox is initialized).
uploadUrl                  | (auto-filled) The URL used to upload the SafeBox attachments (available once the SafeBox is initialized). *Note: this attribute is deprecated.*
publicEncryptionKey        | (auto-filled) The key used to encrypt the SafeBox attachments and/or messages (available once the SafeBox is initialized, only when Double Encryption is enabled).
userEmail                  | (mandatory) The email address of the creator of the SafeBox.
subject                    | (optional) The subject of the SafeBox.
message                    | (contextual\*) The initial message of the SafeBox.
attachments                | (contextual\*) The list of all [Attachment](#attachment) objects of the SafeBox.
participants               | (mandatory) The list of all [Participant](#participant) objects of the SafeBox (at least one recipient).
securityProfileId          | (optional\*\*) The ID of the Security Profile used to create the Security Options of the SafeBox (see [SecurityProfile](#securityprofile) and [SecurityOptions](#securityoptions) objects).
notificationLanguage       | (mandatory) The language used for email notifications sent to the recipients.
userId                     | (read only) The unique ID of the user account of the SafeBox Owner.
enterpriseId               | (read only) The unique ID of the enterprise account of the SafeBox Owner.
status                     | (read only) The current status of the SafeBox (life cycle).
securityProfileName        | (read only) The name of the Security Profile that was used to create the SafeBox.
unreadCount                | (read only) The total count of the unread messages within the SafeBox.
doubleEncryptionStatus     | (read only) The current encryption status of the SafeBox content (i.e. deciphered or key required).
auditRecordPdf             | (read only) The URL of the Audit Record PDF (available after the SafeBox is closed).
secureLink                 | (read only) The URL of the Secure Link that was used to create the SafeBox (when applicable).
secureLinkTitle            | (read only) The Display Name of the Secure Link that was used to create the SafeBox (when applicable).
emailNotificationEnabled   | (optional) Indicates whether email notifications are enabled for the SafeBox Owner or not (enabled by default, can be disabled for example in a context of SafeBox automated creation by a system).
previewUrl                 | (read only) The URL of the SafeBox in the SendSecure Web application.
encryptionKey              | (read only) The encryption key intended for SafeBox participants (when Double Encryption is enabled). It is returned only once at SafeBox creation and then discarded for security reasons.
createdAt                  | (read only) The date on which the SafeBox was created.
updatedAt                  | (read only) The date of last modification of the SafeBox.
assignedAt                 | (read only) The date on which the SafeBox was assigned to the Owner (useful in context of creation via Secure Link).
latestActivity             | (read only) The date of the latest activity that occurred in the SafeBox.
expiration                 | (read only) The date on which the SafeBox is expected to auto-close.
closedAt                   | (read only) The date on which the SafeBox was closed.
contentDeletedAt           | (read only) The date on which the content of the SafeBox was deleted.
securityOptions            | (read only) The [SecurityOptions](#securityoptions) object, containing the whole set of Security Options of the SafeBox.
messages                   | (read only) The list of all [Message](#message) objects of the SafeBox.
downloadActivity           | (read only) The [DownloadActivity](#downloadActivity) object keeping track of all downloads of the SafeBox.
eventHistory               | (read only) The [EventHistory](#eventHistory) object keeping track of all events of the SafeBox.

\* A message is mandatory if no attachments are provided, and at least one attachment is required if no message is provided.
\*\* A Security Profile is always required to create a SafeBox. If no Security Profile ID is specified, the default Security Profile associated to the user will be used.

### SecurityOptions
Builds an object to specify the security options at SafeBox creation, according to the permissions defined in the Security Profile specified in the SafeBox object.
Subset of [Safebox](#Safebox) object.
By default, all attribute values are inherited from the Security Profile.
Once the SafeBox is created, all attributes are no longer editable.

Attribute                  | Definition
---------------------------|-----------
replyEnabled               | (optional) Indicates whether participants can reply or not to a SafeBox.
groupReplies               | (optional) Indicates whether the Guest Participants can see each other or not in the SafeBox.
retentionPeriodType        | (optional) The SafeBox content retention type applied when the SafeBox is closed. Accepted values: ```discard_at_expiration```, ```retain_at_expiration```, ```do_not_discard```.
retentionPeriodValue       | (optional) The value of the retention period.
retentionPeriodUnit        | (optional) The unit of the retention period. Accepted values: ```hours```, ```days```, ```weeks```, ```months```, ```years```.
encryptMessage             | (optional) Indicates whether the messages within the SafeBox will be encrypted or not.
doubleEncryption           | (optional) Indicates whether Double Encryption is enabled or not.
expirationValue            | (contextual\*) The value of the SafeBox open period duration (after which the SafeBox is auto-closed).
expirationUnit             | (contextual\*) The unit of the SafeBox open period duration. Accepted values: ```hours```, ```days```, ```weeks```, ```months```.
expirationDate             | (contextual\*) The auto-close date of the SafeBox.
expirationTime             | (contextual\*) The auto-close time of the SafeBox.
expirationTimeZone         | (contextual\*) The time zone of the auto-close date & time of the SafeBox.
securityCodeLength         | (read only) The length (number of digits) of the security code sent to participants.
codeTimeLimit              | (read only) The validity period of the security code once it is sent to the participant.
allowedLoginAttempts       | (read only) The number of login attempts that are allowed, beyond which the participant access is automatically revoked.
allowRememberMe            | (read only) Indicates whether the participant can be remembered or not on the device used to access the SafeBox.
allowSms                   | (read only) Indicates whether the security code can be sent by SMS or not.
allowVoice                 | (read only) Indicates whether the security code can be sent by voice call or not.
allowEmail                 | (read only) Indicates whether the security code can be sent by email or not.
twoFactorRequired          | (read only) Indicates whether a security code is required or not to authenticate the participant.
autoExtendValue            | (read only) The value of the SafeBox open period auto-extension when a reply is posted near the SafeBox closing date.
autoExtendUnit             | (read only) The unit of the SafeBox open period auto-extension. Accepted values: ```hours```, ```days```, ```weeks```, ```months```.
allowManualDelete          | (read only) Indicates whether the content can be manually deleted or not after the SafeBox is closed.
allowManualClose           | (read only) Indicates whether the SafeBox can be manually closed or not.
encryptAttachments         | (read only) This attribute is always set to true: attachments are actually always encrypted.
consentGroupId             | (read only) The unique ID of the ConsentMessageGroup (see [ConsentMessageGroup](#consentmessagegroup) object).

\* The expiration information (SafeBox auto-close) can be set by specifying either a delay (value + unit) or an actual date (date + time + time zone).

### SecurityProfile
Represents the settings of a Security Profile.
All attributes are read only.
All attributes are composed of two properties: value and modifiable. The value field is as described below and the modifiable field indicates whether the value can be modified or not (let the user choose) at SafeBox creation.

Attribute                  | Definition
---------------------------|-----------
id                         | The unique ID of the Security Profile.
name                       | The name of the Security Profile.
description                | The description of the Security Profile.
createdAt                  | The Security Profile creation date.
updatedAt                  | The Security Profile last modification date.
allowedLoginAttempts       | The number of login attempts that are allowed, beyond which the participant access is automatically revoked.
allowRememberMe            | Indicates whether the participant can be remembered or not on the device used to access the SafeBox.
allowSms                   | Indicates whether the security code can be sent by SMS or not.
allowVoice                 | Indicates whether the security code can be sent by voice call or not.
allowEmail                 | Indicates whether the security code can be sent by email or not.
codeTimeLimit              | The validity period of the security code once it is sent to the participant.
codeLength                 | The length (number of digits) of the security code sent to participants.
autoExtendValue            | The value of the SafeBox open period auto-extension when a reply is posted near the SafeBox closing date.
autoExtendUnit             | The unit of the SafeBox open period auto-extension. Accepted values: ```hours```, ```days```, ```weeks```, ```months```.
twoFactorRequired          | Indicates whether a security code is required or not to authenticate the participant.
encryptAttachments         | This attribute is always set to true: attachments are actually always encrypted.
encryptMessage             | Indicates whether the messages within the SafeBox will be encrypted or not.
expirationValue            | The value of the SafeBox open period duration (after which the SafeBox is auto-closed).
expirationUnit             | The unit of the SafeBox open period duration. Accepted values: ```hours```, ```days```, ```weeks```, ```months```.
replyEnabled               | Indicates whether participants can reply or not to a SafeBox.
groupReplies               | Indicates whether the Guest Participants can see each other or not in the SafeBox.
doubleEncryption           | Indicates whether Double Encryption is enabled or not.
retentionPeriodType        | The SafeBox content retention type applied when the SafeBox is closed. Accepted values: ```discard_at_expiration```,  ```retain_at_expiration```, ```do_not_discard```.
retentionPeriodValue       | The value of the retention period.
retentionPeriodUnit        | The unit of the retention period. Accepted values: ```hours```, ```days```, ```weeks```, ```months```, ```years```.
allowManualDelete          | Indicates whether the content can be manually deleted or not after the SafeBox is closed.
allowManualClose           | Indicates whether the SafeBox can be manually closed or not.
allowForSecureLinks        | Indicates whether the Security Profile can be used or not for Secure Links.
useCaptcha                 | Indicates whether a verification through Captcha is required for the Secure Link.
verifyEmail                | Indicates whether the verification of the email of the Secure Link user is required or not.
distributeKey              | Indicates whether a copy of the participant key will be sent or not by email to the SafeBox Owner when Double Encryption is enabled.
consentGroupId             | The unique ID of the ConsentMessageGroup (see [ConsentMessageGroup](#consentmessagegroup) object).


### UserSettings
Builds an object to retrieve the SendSecure options of the current user.
All attributes are read only.

Attribute                  | Definition
---------------------------|-----------
maskNote                   | Indicates whether the user wants the messages to be masked or not when accessing a SafeBox with message encryption enabled.
openFirstTransaction       | Indicates whether the user wants the contents of the first SafeBox in the list to be automatically displayed or not when accessing the SendSecure interface.
markAsRead                 | Indicates whether the user wants the unread messages to be automatically marked as read or not when accessing a SafeBox.
markAsReadDelay            | The delay (in seconds) after which the messages are automatically marked as read.
rememberKey                | Indicates whether the user accepts or not that the participant key is remembered on the client side to allow subsequent accesses to SafeBoxes having Double Encryption enabled.
defaultFilter              | The default SafeBox list filter as defined by the user.
recipientLanguage          | The language in which the user needs the SafeBox recipients to be notified by email and access the SafeBox on their side.
secureLink                 | The [PersonnalSecureLink](#personalSecureLink) object representing the Personal Secure Link information of the user.
createdAt                  | The creation date of the user settings.
updatedAt                  | The last modification date of the user settings.

# License

sendsecure-java is distributed under [MIT License](https://github.com/xmedius/sendsecure-java/blob/master/LICENSE).

# Credits

sendsecure-java is developed, maintained and supported by [XMedius Solutions Inc.](https://www.xmedius.com?source=sendsecure-java)
The names and logos for sendsecure-java are trademarks of XMedius Solutions Inc.

![XMedius Logo](https://s3.amazonaws.com/xmc-public/images/xmedius-site-logo.png)
