package com.codingnight.android.insetsanimation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class ConversationFragment : Fragment() {

    private lateinit var conversationRecyclerview: RecyclerView
    private lateinit var messageHolder: LinearLayout
    private lateinit var messageEdittext: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conversation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        conversationRecyclerview = view.findViewById(R.id.conversation_recyclerview)
        messageHolder = view.findViewById(R.id.message_holder)
        messageEdittext = view.findViewById(R.id.message_edittext)

        conversationRecyclerview.adapter = ConversationAdapter()

        val deferringInsetsListener = RootViewDeferringInsetsCallback(
            persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
            deferredInsetTypes = WindowInsetsCompat.Type.ime()
        )
        ViewCompat.setWindowInsetsAnimationCallback(view, deferringInsetsListener)
        ViewCompat.setOnApplyWindowInsetsListener(view, deferringInsetsListener)

        ViewCompat.setWindowInsetsAnimationCallback(
            messageHolder,
            TranslateDeferringInsetsAnimationCallback(
                view = messageHolder,
                persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
                deferredInsetTypes = WindowInsetsCompat.Type.ime(),
                // We explicitly allow dispatch to continue down to binding.messageHolder's
                // child views, so that step 2.5 below receives the call
                dispatchMode = WindowInsetsAnimationCompat.Callback.DISPATCH_MODE_CONTINUE_ON_SUBTREE
            )
        )
        ViewCompat.setWindowInsetsAnimationCallback(
            conversationRecyclerview,
            TranslateDeferringInsetsAnimationCallback(
                view = conversationRecyclerview,
                persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
                deferredInsetTypes = WindowInsetsCompat.Type.ime()
            )
        )

        ViewCompat.setWindowInsetsAnimationCallback(
            messageEdittext,
            ControlFocusInsetsAnimationCallback(messageEdittext)
        )
    }
}