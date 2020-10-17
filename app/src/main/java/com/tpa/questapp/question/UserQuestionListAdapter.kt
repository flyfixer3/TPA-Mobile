package com.tpa.questapp.question

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.tpa.questapp.model.Question
import com.tpa.questapp.model.User
import kotlinx.android.synthetic.main.user_question_list.view.*

class UserQuestionListAdapter(
    var ctx: Context,
    var resource: Int,
    private var items:List<Question>
) : ArrayAdapter<Question>(ctx, resource, items)  {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(ctx)
        val view:View = layoutInflater.inflate(resource, null)

        val question: TextView = view.questionUserListTxt

        var mItem: Question = items[position]
        question.setText(mItem.question)

        return view
    }
}