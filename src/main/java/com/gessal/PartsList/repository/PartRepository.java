package com.gessal.PartsList.repository;

import com.gessal.PartsList.entity.Part;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PartRepository extends CrudRepository<Part, Integer> {
    /* Запрос возвращает минимальное значение столбца "count" среди всех комплектующих,
     * необходимых для сборки компьютера. Таким образом можно определить максимальное
     * количество компьютеров, которые можно собрать из имеющихся деталей. */
    @Query(value = "select min(count) from part where part.need=1", nativeQuery = true)
    Integer selectMinCount();

    /* Запрос возвращает комплектующее по его ID.
     * Просто подставить возвращаемое значение класса Part не получится. */
    Optional<Part> findById(Integer id);

    /* Запрос выводит список всех комплектующих по их названию. */
    Page<Part> findByName(String name, Pageable page);

    /* Запрос возвращает список всех комплектующих в базе */
    Page<Part> findAll(Pageable pageable);

    /* Запрос возвращает список всех необходимых, либо опциональных комплектующих
     * в зависимости от переданного значения "need" */
    Page<Part> findByNeed(Boolean need, Pageable page);

    /*Запрос возвращает список всех необходимых, либо опциональных комплектующих
     * в зависимости от переданного значения "need", и название которых совпадает
     * со значением "name"*/
    Page<Part> findByNameAndNeed(String name, Boolean need, Pageable page);
}
