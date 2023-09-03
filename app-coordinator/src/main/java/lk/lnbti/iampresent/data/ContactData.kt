package lk.lnbti.iampresent.data

import android.util.Log

/**
 * Represents a contact with a name and phone number.
 *
 * @property name The name of the contact.
 * @property phone The phone number of the contact.
 */
data class Contact(
    var name: String,
    var phone: String,
)

/**
 * A singleton object that manages a list of contacts.
 */
object ContactData {
    /**
     * The list of contacts stored in memory.
     */
    val contacts: MutableList<Contact> = mutableListOf(
        Contact(
            name = "Udeni",
            phone = "1234567890"
        ),
        Contact(
            name = "Sara",
            phone = "0784567890"
        ),
        Contact(
            name = "Nimal",
            phone = "0733456789"
        ),
        Contact(
            name = "Gayani",
            phone = "0774567890"
        ),
        Contact(
            name = "San",
            phone = "0714567890"
        ),
        Contact(
            name = "Dee",
            phone = "0744567890"
        ),
        Contact(
            name = "Nuwan",
            phone = "1234567890"
        ),
        Contact(
            name = "Mali",
            phone = "0784567890"
        ),
        Contact(
            name = "Raj",
            phone = "0733456789"
        ),
    )

    /**
     * Retrieves a contact by its name.
     *
     * @param contactName The name of the contact to retrieve.
     * @return The [Contact] instance with the matching name, or null if not found.
     */
    fun getContact(contactName: String?): Contact? {
        try {
            return contacts.first { it.name == contactName }
        } catch (e: Exception) {
            Log.d("Search contact", e.message.toString())
            return null
        }
    }

    /**
     * Adds a new contact to the list.
     *
     * @param contact The [Contact] instance to be added.
     */
    fun addContact(contact: Contact) {
        contacts.add(contact)
    }

    /**
     * Updates an existing contact with new information.
     *
     * @param originalContactName The name of the contact to be updated.
     * @param updatedContact The updated [Contact] information.
     */
    fun updateContact(originalContactName: String, updatedContact: Contact) {
        var contact = contacts.find { it.name == originalContactName }
        contact?.let {
            it.name = updatedContact.name
            it.phone = updatedContact.phone
        }
    }

    /**
     * Deletes a contact by its name.
     *
     * @param contactName The name of the contact to be deleted.
     */
    fun deleteContact(contactName: String) {
        contacts.remove(contacts.first { it.name == contactName })
    }
}

