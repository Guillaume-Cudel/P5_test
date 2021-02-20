package todoList;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.annotation.Nullable;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.List;

import repositories.ProjectDataRepository;
import repositories.TaskDataRepository;

public class TaskViewModel extends AndroidViewModel {

    private TaskDataRepository taskDataSource;
    private ProjectDataRepository projectDataSource;
   // private final Executor executor;

    // DATA
    @Nullable
    private LiveData<Project> currentProject;
    private LiveData<List<Task>> mAllTasks;

   /* public TaskViewModel(TaskDataRepository taskDataSource, ProjectDataRepository projectDataSource) {
        this.taskDataSource = taskDataSource;
        this.projectDataSource = projectDataSource;
       // this.executor = executor;
    }*/

    public TaskViewModel(Application application){
        super(application);
        taskDataSource = new TaskDataRepository(application);
        projectDataSource = new ProjectDataRepository(application);

    }

   /* public void init(long projectId){
        if(this.currentProject != null){
            return;
        }
        currentProject = projectDataSource.getProject(projectId);
    }*/


    //-----------
    // FOR TASK
    //-----------

    public LiveData<List<Task>> getAllTasks(){
        return taskDataSource.getAllTasks();
    }

    public LiveData<List<Task>> getTasksInProject(long projectId){
        return taskDataSource.getTasksInProject(projectId);
    }

    public LiveData<List<Task>> getOneTask(long taskId){
        return taskDataSource.getOneTask(taskId);
    }

    public void insert(Task task){ taskDataSource.insertTask(task);}

    public void deleteTask(Task task){ taskDataSource.deleteTask(task); }

    public void update(Task task){ taskDataSource.update(task);}

   /* public void createTask(Task task){
        executor.execute(() -> {
            taskDataSource.insertTask(task);
        });
    }

    public void deleteTask(Task task){
        executor.execute(() -> {
            taskDataSource.deleteTask(task);
        });
    }

    public void updateTask(Task task){
        executor.execute(() -> {
            taskDataSource.update(task);
        });
    }*/


}
