What does the RIS2N3 tool do?  
================================  

    
Creating the Publication and Author objects
-------------------------


For each provided RIS file the tool loops through line by line, looking to identify entries with string matching. When a complete entry is identified the item’s Title is extracted, as well as each of the Author’s names.

At this point, if the title is unique in the data the tool has already searched, a Publication object is created. If it’s not unique, the pre-existing object is used, and its data augmented.

With this Publication object, the tool now iterates over the Author values that were extracted. If the name hasn’t been found before, they are added to a unique list.

If the Author has been previously entered, and there is more information (middle initial, full middle name, full first name etc.) the tool augments that entry.

With our list of Author objects which have been processed, the tool now adds them to the Publication object.
  
  
  
Name disambiguation
-------------------------


It was previously mentioned that the tool checks each RIS entry’s list of authors against a list of unique Author objects that have been accumulated in the process up to that point. To determine whether or not an author is unique, a series of string matches are done. First, the last name is compared. Then the first initial (or full first name if available). If available the middle initial or full middle name is also compared.

If the newer name has more information than the previous entry, and is deemed to be the same person, then the tool updates the Author object to reflect this. If there is no author previously entered that matches, a new unique Author object is created.



This way, by the time the tool has processed all available information, there is a complete list of unique authors, with every bit of information known about them, regardless of how many entries, and combinations of their name and initials have appeared in the raw data.



There is still a possibility of conflated author names with this process, but given the information provided is the best case scenario (preventing as much manual intervention as possible).
  
RIS Type matching
-------------------------


When the tool created the Publication objects, it attached the raw RIS data as an attribute. After the Publication and Author objects have been compiled, the Publication objects are then iterated over, matching the RIS data to its respective type (JOUR, BOOK etc.) The tool has a series of templates to allow for the variations in style that each type has in its RIS format.
  
Creating the raw N3
-------------------------

After the tool has matched the RIS to its type, the raw N3 is created. This is done by creating Strings containing the relevant basic URIs and definitions relating to the information contained in the RIS entry.

After all the Publication objects have been iterated over, the tool now has a long list of N3 raw strings that VIVO can ingest.
  
  
Linking entities
-------------------------

Before the tool ingests the N3 via the Jena library, it creates relationships via the various entities that it has created. For example, a Journal Article has a corresponding Journal. At this stage, the tool loops through these types of objects (Journals -> Journal Articles, Conferences -> Papers etc.) and creates the N3 to define their relationship.  

Finally, we create the raw N3 that defines the Authors. Up until this point, the tool has just been using their URI – this gives meaning to what that URI represents, and links them to their Publications.
  
  
  
Jena usage and options for greater VIVO integration
-------------------------


The final stage is where the tool utilizes the Jena library and connects to the Model stored in the database, and ingests the N3 that was created previously.



It is possible that this base functionality could be expanded to reach closer to capabilities of the Harvester. For example, utilizing Jena, the tool could use SPARQL queries to find out if any of its unique authors are already in VIVO, and if so, update rather can create them. The same can be done for publications, Journals, etc.


