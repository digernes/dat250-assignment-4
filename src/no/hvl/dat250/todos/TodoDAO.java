package no.hvl.dat250.todos;

import javax.persistence.*;
import java.util.List;

public class TodoDAO implements Dao<Todo> {

    private static final String PERSISTENCE_UNIT_NAME = "dat250";
    private static EntityManagerFactory factory;

    public TodoDAO() {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }


    @Override
    public Todo getById(Long id) {
        EntityManager em = factory.createEntityManager();
        Todo todo = new Todo();
        try {
            todo = em.find(Todo.class, id);
        }finally {
            em.close();
        }
        if(todo == null) {
            throw new EntityNotFoundException("Found no todo with id " + id);
        }
        return todo;
    }

    @Override
    public List<Todo> getAll() {
        EntityManager em = factory.createEntityManager();
        List<Todo> todoList;

        try{
            Query q = em.createQuery("select t from Todolist t");
            todoList = q.getResultList();
        }
        finally {
            em.close();
        }
        if(todoList == null) {
            throw new EntityNotFoundException("No tasks exist in the todolist database");
        }
        return todoList;
    }

    @Override
    public void save(Todo todo) {
        EntityManager em = factory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(todo);
            em.getTransaction().commit();
        }finally {
            em.close();
        }
    }

    @Override
    public void update(Todo todo) {
        EntityManager em = factory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(todo);
            em.getTransaction().commit();
        }finally {
            em.close();
        }
    }

    @Override
    public void delete(Todo todo) {
        EntityManager em = factory.createEntityManager();
        try {
            Todo toRemove = em.find(Todo.class, todo.getId());
            em.getTransaction().begin();
            em.remove(toRemove);
            em.getTransaction().commit();
        }finally {
            em.close();
        }
    }
}
