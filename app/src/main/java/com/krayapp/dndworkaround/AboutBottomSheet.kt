package com.krayapp.dndworkaround

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.color.MaterialColors
import com.krayapp.dndworkaround.databinding.AboutBottomsheetLayoutBinding

class AboutBottomSheet : BottomSheetDialogFragment() {

    private var vb: AboutBottomsheetLayoutBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = AboutBottomsheetLayoutBinding.inflate(inflater)
        return vb!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        colorNavBar()
        vb?.next?.setOnClickListener { dismiss() }
    }

    private fun colorNavBar() {
        val color =
            MaterialColors.getColor(requireContext(), R.attr.bottomSheetBottomColor, Color.BLACK)
        dialog?.window?.navigationBarColor = color
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vb = null
    }
}