package com.cleanup.todoc;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cleanup.todoc.dao.TaskDao;
import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class TaskDaoTest {

    // FOR DATA
    private TaskDao taskDao;
    private TodocDatabase database;

    // DATA SET FOR TEST
    private static long TASK_ID = 1;
    private static long TASK_ID2 = 2;
    private static long TASK_ID3 = 3;

    // todo récupérer un projet du tableau
    private static Project[] ARRAY_PROJECT_DEMO = Project.getAllProjects();
    private static Project FIRST_PROJECT_DEMO = ARRAY_PROJECT_DEMO[0];
    private static Project SECOND_PROJECT_DEMO = ARRAY_PROJECT_DEMO[1];
    private static Task TASK_DEMO = new Task(TASK_ID, FIRST_PROJECT_DEMO.getId(),"Laver les vitres", new Date().getTime());
    private static Task TASK_DEMO2 = new Task(TASK_ID2, SECOND_PROJECT_DEMO.getId(),"Passer l'aspirateur", new Date().getTime());
    private static Task TASK_DEMO3 = new Task(TASK_ID3, SECOND_PROJECT_DEMO.getId(),"Faire la vaisselle", new Date().getTime());


    @Before
    public void createDb(){
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, TodocDatabase.class).allowMainThreadQueries().build();
        taskDao = database.taskDao();
    }

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @After
    public void closeDb() throws IOException{
        database.close();
    }


    @Before
    public void getItemsWhenNoItemInserted() throws InterruptedException {
        // TEST
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getAllTasks());
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void insertAndGetProject() throws InterruptedException {

        this.database.projectDao().insertProject(FIRST_PROJECT_DEMO);

        // TEST
       Project project = LiveDataTestUtil.getValue(this.database.projectDao().getProject(FIRST_PROJECT_DEMO.getId()));
        assertTrue(project.getName().equals(FIRST_PROJECT_DEMO.getName()) && project.getId() == FIRST_PROJECT_DEMO.getId());
    }

    @Test
    public void insertAndGetTasks() throws InterruptedException{
        this.database.projectDao().insertProject(FIRST_PROJECT_DEMO);
        this.database.projectDao().insertProject(SECOND_PROJECT_DEMO);
        this.database.taskDao().insertTask(TASK_DEMO);
        this.database.taskDao().insertTask(TASK_DEMO2);
        this.database.taskDao().insertTask(TASK_DEMO3);

        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getAllTasks());
        assertTrue(tasks.size() == 3);

        List<Task> tasksInFirstProject = LiveDataTestUtil.getValue(this.database.taskDao().getTasksWithProjectId(FIRST_PROJECT_DEMO.getId()));
        assertTrue(tasksInFirstProject.size() == 1);
    }


   /* @Test
    public void insertAndUpdateTask() throws InterruptedException {
       // update task added & re-save it
        this.database.taskDao().insertTask(TASK_DEMO);
        Task taskAdded = LiveDataTestUtil.getValue(this.database.taskDao().getOneTask(TASK_DEMO.getId())).get(0);
        this.database.taskDao().updateTask(taskAdded);

        //TEST
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getAllTasks());
        assertTrue(tasks.size() == 1 );
    }*/

    @Test
    public void insertAndDeleteTask() throws InterruptedException {
        // BEFORE : Adding demo task. Next, get the task added & delete it.
        this.database.projectDao().insertProject(FIRST_PROJECT_DEMO);
        this.database.taskDao().insertTask(TASK_DEMO);
        Task taskAdded = LiveDataTestUtil.getValue(this.database.taskDao().getOneTask(TASK_DEMO.getId())).get(0);
        assertTrue(taskAdded.getId() == TASK_DEMO.getId());

        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getAllTasks());
        assertTrue(tasks.size() == 1 );

        this.database.taskDao().deleteTask(taskAdded);
        assertTrue(tasks.isEmpty());
    }

}
