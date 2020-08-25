package com.linksbox.exception;

/**
 * This enum contains all the keys that should be known to the client (the angular application used by a human user)
 * The ErrorKey will be mapped from within that application and will be mapped to understandable text messages.
 *
 * Example: if the user searches for an object with the ID -87689803947789234343332 and no object can be found
 * (because everything else would be just absurd) then the service would return UNKNOWN_ID but the client
 * would display a nice localized message like "Zu dieser ID konnte nichts gefunden werden"
 */
public enum ErrorKey {

	NONE,
	TAG_NAME,
	LINK_URL,
	UNKNOWN_UUID
}
