# Previously on Building A Note Taking App

In the [**previous tutorial**](https://androidpirate.github.io/NoteAppSqliteQuery/ "**previous tutorial**"), we created our own **SQLite database** and implemented a query
method in our **SQLiteOpenHelper class** to get notes from the database and displayed a warning message if the list is empty.



## NoteApp SQLiteInsert – Tutorial 5

Start by cloning/forking the app from the link below:

[**NoteApp SQLiteInsert - Tutorial 5**](https://github.com/androidpirate/NoteAppSqliteInsert "**NoteApp SQLiteInsert - Tutorial 5**")



### Goal of This Tutorial

The goal of this tutorial is to implement a new method in **NoteDbHelper** to insert new notes into the database. We are also going to
implement a new activity; **EditActivity** that will handle both inserting a new note and updating an existing one.

**Content Values** is an object that is used to store data using key-value pairs. (**Yeah, similar to Intent Extras or a Bundle**)
It uses column names in a database for keys and used to insert or update new data.



### What’s in Starter Module?

Starter module already has a fully functional **RecyclerView** that works with a **SQLite database** to get a list of notes.

**If you have never implemented an SQLite database before, I highly recommend to follow this tutorial closely.**

You can follow the steps below and give it a shot yourself, and if you stuck at some point, check out the **solution module** or the rest
of the tutorial.



### Steps to Build

1. Add a new menu file: **main_menu.xml**
2. Add a new menu item: **ADD**
3. Inflate the menu in **MainActivity**
4. Implement **ADD** menu item to start **EditActivity**
5. Add a new activity: **EditActivity** and its layout file
6. Add two **EditText** widgets in **activity_edit.xml**, one for note title, one for note description
7. Add a new menu file: **edit_menu.xml**
8. Add a new menu item: **SAVE**
9. Implement **SAVE** menu item to save the note to the database and return to **MainActivity**



### App Bar (Action Bar)

**App Bar** or formerly known as **Action Bar** is a special toolbar, that is used for branding, navigation, and search. In **NoteApp**, we
are going to use **App Bar** to display specific actions that will handle special tasks, such as inserting a new **Note** to the database.
To create the menu for **App Bar**, create a new **menu** directory under the **res** directory and add a new **menu layout file**;
 **main_menu.xml**  (Check out [**this guide**](https://developer.android.com/training/appbar/setting-up "**this guide**") to setup a
 **Toolbar** as an **App Bar**):


```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto">

    <item
        android:id="@+id/new_note_action"
        android:title="@string/new_note"
        app:showAsAction="always"/>

</menu>
```


There is only one item (**new_note_action**) on our menu, which will be used to add new notes to the database.

Once the menu layout is ready to go, switch to **MainActivity** and override the **onCreateOptionsMenu()** callback **(CTRL + O, Override
  methods dialog)** and inflate **main_menu.xml**:


```java
public class MainActivity extends AppCompatActivity
  implements NoteAdapter.NoteClickListener {
    // Fields and callbacks are excluded for simplicity

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
```


If you run the app at this point, you will see the **ADD** action item in your **MainActivity** 's **App Bar**. But clicking on **ADD**,
will not do anything. In order to make it work properly, we need to override **onOptionsItemSelected()** callback and implement what is
going to happen when the user clicks on **ADD**:


```java
public class MainActivity extends AppCompatActivity
    implements NoteAdapter.NoteClickListener {
    // Fields and callbacks are excluded for simplicity

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_note_action:
                // Start EditActivity
                Intent intent = new Intent(this, EditActivity.class);
                intent.putExtra(EditActivity.EXTRA_NOTE, new Note());
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
```


As soon as the user clicks on **ADD**, a new **Intent** will be created to start **EditActivity**, which also has a new **Note** as an
**Intent Extra**!



### EditActivity

Android Studio is going to give us a warning message there is no such a class as **EditActivity** as soon as we create an explicit intent,
 so go ahead and create a new activity; **EditActivity** and its layout file, **activity_edit.xml**:


```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.example.android.noteappsqlite.EditActivity">

    <EditText
        android:id="@+id/et_title"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textSize="22sp"
        android:textColor="@android:color/black"
        android:hint="@string/title_hint"
        android:inputType="textMultiLine"
        android:layout_margin="8dp"/>

    <EditText
        android:id="@+id/et_description"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textSize="22sp"
        android:textColor="@android:color/black"
        android:hint="@string/description_hint"
        android:layout_below="@id/et_title"
        android:inputType="textMultiLine"
        android:layout_margin="8dp"/>

</RelativeLayout>
```

**activity_edit.xml** has only two **EditText** elements, which is used to input note title and description.

Now, let's get our new note as an **Intent Extra** in **EditActivity**:


```java
public class EditActivity extends AppCompatActivity {
    public static final String EXTRA_NOTE = "extra-note";
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        // Get the note from intent extra
        if(getIntent() != null) {
            note = getIntent().getParcelableExtra(EXTRA_NOTE);
        }
    }
}
```


**(Hold on, hold on! Why don't we create a new Note in EditActivity, instead of creating it in MainActivity and send it as an Intent Extra???)**

The answer is, we are also going to use **EditActivity** to update an existing **Note** in the list, and in order to open up the correct
**Note** from the list, we either going to send its **id** as an **Intent Extra** and query the database for that specific id to get the
note from the database in **EditActivity**, or we are going to put the note as an extra and send it. In my opinion, it is more efficient to
send it as an extra. **(I might be wrong...)**

Once we get our note, it is all about setting a **TextWatcher** for **EditText** elements and listens for a change:


```java
public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        .
        .
        EditText title = findViewById(R.id.et_title);
        if(note.getTitle() != null) {
            title.setText(note.getTitle());
        }
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                note.setTitle(s.toString());
            }
        });

        EditText description = findViewById(R.id.et_description);
        if(note.getDescription() != null) {
            description.setText(note.getDescription());
        }
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                note.setDescription(s.toString());
            }
        });
    }
}
```


I have never been a big fan of **TextWatcher** either. **(Maybe one day, I will write an article/tutorial about how to minimize the boiler
plate code it uses)** The idea is, if we get an existing note, we are setting **EditText** elements with the data we got from the note,
otherwise, it would be empty.



### Inserting Note to Database

First of all, we need to implement the insert method that we will use in **NoteDbHelper** when the user wants to insert a new **Note**.
Android framework, uses **Content Values** to store data as key-value pairs, as column names defined in **NoteContract** to be keys and data
we want to store as values ([**SQLite Data Types**](http://www.sqlite.org/datatype3.html "**SQLite Data Types**")):


```java
public class NoteDbHelper extends SQLiteOpenHelper {
  // Fields and callbacks are excluded for simplicity
  public void insertNote(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(NoteEntry.NOTE_TITLE, note.getTitle());
            values.put(NoteEntry.NOTE_DESCRIPTION, note.getDescription());
             // No id is needed to insert a note to database since
             // SQLite automatically increments the id numbers
            db.insertOrThrow(NoteEntry.TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e){
            Log.d(TAG, "Problem inserting note into database.");
        } finally {
            db.endTransaction();
        }
    }
}
```


Here we get a **writable** instance of **Note database**, which allows inserting new data, as well as updating existing data. **(readable
instance we used in our query method only reads data)** There is also, mysterious **beginTransaction()** method, which allows us to start
a transaction to enter chunks of data at a time to increase the efficiency...**(Database operations are costly...Time is money friend!)**

**(Great! But insertNote() method only inserts one note at a time...)** The other advantage of using a transaction if inserting data fails
for whatever reason, the transaction will be aborted at the point of failure and all the operations will be rolled back to its previous
state.

Also, notice **insertOrThrow() method**, which throws an exception if inserting data fails, so we catch the exception and write a log
message to **debug log**, indicating there is a problem inserting a note to the database. **(Everything has a beginning, has an end.)**
Finally, we end the transaction.

When the user is done filling in the fields, it is time for us to save the note to **NoteDatabase**. To do that, we will create a similar
**App Bar** action item in **MainActivity**. Let's start with creating a new menu file, **edit_menu.xml** under **menu** directory:


```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto">

    <item
        android:id="@+id/save_note_action"
        android:title="@string/save_note"
        app:showAsAction="always"/>

</menu>
```


As the previous menu, we only have one action item, **(save_note_action)** and we will do the same thing as we did before, inflating the
menu and implementing **SAVE** action menu item:


```java
public class EditActivity extends AppCompatActivity {
    // Fields and callbacks are excluded for simplicity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note_action:
                NoteDbHelper noteDbHelper = NoteDbHelper.getInstance(this);
                noteDbHelper.insertNote(note);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
```


The important part is where we implement **SAVE** action item. First, we get an instance of **NoteDbHelper** and we use the **insertNote()
method** to enter the note into **note database**. Finally, we create an **Intent** to take us back to **MainActivity**. And that's it!
Now you can run the app and add new notes!



### What's In Next Tutorial

In our next tutorial, we are going to implement a swipe to delete functionality in **RecyclerView** along with its counterpart deleteNote()
method in **NoteDbHelper**.



### Resources

1. [Android Developer Guides](https://developer.android.com/guide/ "Android Developer Guides") by Google
2. [SQLite Official Website](http://www.sqlite.org/index.html "SQLite Official Website")
