/*
 * Copyright (C) 2014 Toshiaki Maki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package am.ik.categolj2.domain.model;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"histories", "contents"})
@Entity
@Indexed
@Table(name = "ENTRY", indexes = {
        @javax.persistence.Index(columnList = "LAST_MODIFIED_DATE"),
        @javax.persistence.Index(columnList = "CREATED_BY")
})
public class Entry extends AbstractAuditableEntiry<Integer> {

    private static final long serialVersionUID = 1L;
    @GeneratedValue
    @Id
    @Column(name = "ENTRY_ID")
    private Integer entryId;
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "TITLE")
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private String title;
    @NotNull
    @Size(min = 1, max = 65536)
    @Column(name = "CONTENTS")
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private String contents;
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "FORMAT")
    private String format;
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true, mappedBy = "entry")
    @OrderBy("CATEGORY_ORDER ASC")
    private List<Category> category;
    @Column(name = "PUBLISHED")
    @Basic(optional = false)
    @Field(index = Index.YES, analyze = Analyze.NO, store = Store.NO)
    private boolean published;
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true, mappedBy = "entry")
    @OrderBy("CREATED_DATE")
    @JsonIgnore
    private List<EntryHistory> histories;

    public Entry(Integer entryId, String title) {
        this.entryId = entryId;
        this.title = title;
    }

    @Override
    @Transient
    public Integer getId() {
        return entryId;
    }

    @Override
    @Transient
    @JsonIgnore
    public boolean isNew() {
        return entryId == null;
    }

    // public String toString() {
    // return "Entry[" + entryId + "]";
    // }
}
