/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codelabs.state.todo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TodoViewModel : ViewModel() {

//    private var _todoItems = MutableLiveData(listOf<TodoItem>())
//    val todoItems: LiveData<List<TodoItem>> = _todoItems
//  위랑 아래는 쌔임쌔임, 근데 컴포즈만 사용하는 스테이트일 때만 쓸 것.
//  뷰 시스템에서도 사용한다면 LiveData를 사용하는 게 낫다.
    var todoItems = mutableStateListOf<TodoItem>()
        private set

    private var currentEditPosition by mutableStateOf(-1)
    //  Compose는 State<T>뿐만 아니라 아래처럼 regular function을 호출해서도
    //  옵저빙이 가능하다.
    //  그래서 이걸 Observe하면 todoItems이나 currentEditPosition이
    //  변경되었을 때에도 이벤트를 수신함.
    val currentEditItem: TodoItem?
        get() = todoItems.getOrNull(currentEditPosition)

    fun addItem(item: TodoItem) {
        todoItems.add(item)
    }

    fun removeItem(item: TodoItem) {
        todoItems.remove(item)
        onEditDone()
    }

    // event: onEditItemSelected
    fun onEditItemSelected(item: TodoItem) {
        currentEditPosition = todoItems.indexOf(item)
    }

    // event: onEditDone
    fun onEditDone() {
        currentEditPosition = -1
    }

    // event: onEditItemChange
    fun onEditItemChange(item: TodoItem) {
        val currentItem = requireNotNull(currentEditItem)
        require(currentItem.id == item.id) {
            "You can only change an item with the same id as currentEditItem"
        }

        todoItems[currentEditPosition] = item
    }
}
