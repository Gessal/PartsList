package com.gessal.PartsList.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Part {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;                                    // ID
    private String name;                                   // название
    private Boolean need;                                  // флаг необходимости для сборки
    private Integer count;                                 // количество на складе

    /* Геттеры и сеттеры для переменных класса.
     * Для ID сеттер не нужен, т.к. Hibernate будет генерировать его значение автоматически.
     * ------------------------------------------------------------------------------------ */
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getNeed() {
        return need;
    }

    public void setNeed(Boolean is_need) {
        this.need = is_need;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
    // ------------------------------------------------------------------------------------
}
