package com.lsxy.call.center.api.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.util.List;
/**
 * Created by liuws on 2016/10/29.
 */
@XStreamAlias("enqueue")
public class Equeue {
    private Condition condition;

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public class Condition{
        private Integer expired;
        private And and;
        private Or or;

        public Integer getExpired() {
            return expired;
        }

        public void setExpired(Integer expired) {
            this.expired = expired;
        }

        public And getAnd() {
            return and;
        }

        public void setAnd(And and) {
            this.and = and;
        }

        public Or getOr() {
            return or;
        }

        public void setOr(Or or) {
            this.or = or;
        }
    }

    public class And{
        private List<Skill> skills;

        public List<Skill> getSkills() {
            return skills;
        }

        public void setSkills(List<Skill> skills) {
            this.skills = skills;
        }
    }

    public class Or{
        private List<Skill> skills;

        public List<Skill> getSkills() {
            return skills;
        }

        public void setSkills(List<Skill> skills) {
            this.skills = skills;
        }
    }

    public class Skill{
        private String name;
        private Double weight;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getWeight() {
            return weight;
        }

        public void setWeight(Double weight) {
            this.weight = weight;
        }
    }
}


