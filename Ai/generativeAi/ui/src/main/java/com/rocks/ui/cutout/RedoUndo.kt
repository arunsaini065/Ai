package com.rocks.ui.cutout

import android.graphics.Bitmap

class RedoUndo {

    private val _actions = mutableListOf<DrawViewState>()

    private var _currentIndex = -1

    private var _currentAction: DrawViewState?=null

    lateinit var redoUndoCallback:()->Unit

    private var _bitmap:Bitmap?=null

     private var action: DrawViewState?=null



    fun getAction() = _currentAction


    fun doAction(action: DrawViewState) {
        if (this.action!=null) {
            if (_currentIndex < _actions.size - 1) {

                _actions.subList(_currentIndex + 1, _actions.size).clear()

            }

            _actions.add(this.action!!)

            _currentIndex++
        }
        this.action=action
    }

    fun isStackFull () = _currentIndex >= _actions.size

    fun undoAction() {

        if (_currentIndex >= 0) {

            val action = _actions[_currentIndex]

            _currentIndex--

            _currentAction=action
            if (::redoUndoCallback.isInitialized){
                redoUndoCallback()
            }
        }

    }

    fun redoAction() {

        if (_currentIndex < _actions.size - 1) {

            if(_currentIndex==-1 && _actions.size>1){
                _currentIndex=0
            }

            _currentIndex++


            var action = _actions[_currentIndex]

             if (action==_currentAction){
                 if (_currentIndex+1>=_actions.size) {
                     action = this.action!!
                 }else {
                     action = _actions[_currentIndex+1]
                 }

             }

            _currentAction=action



        }else{
            _currentAction=this.action
        }

        if (::redoUndoCallback.isInitialized){
            redoUndoCallback()
        }


    }

    fun getOriginalBimap(): Bitmap? {
        return _bitmap?.cloneBitmap()
    }

    fun setOriginalBimap(copy: Bitmap?) {
        _bitmap=copy
    }

}

