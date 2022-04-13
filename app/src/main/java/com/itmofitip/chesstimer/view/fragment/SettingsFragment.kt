package com.itmofitip.chesstimer.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.itmofitip.chesstimer.R
import com.itmofitip.chesstimer.presenter.SettingsPresenter
import com.itmofitip.chesstimer.repository.SwitchType
import com.itmofitip.chesstimer.utilities.APP_ACTIVITY
import com.itmofitip.chesstimer.utilities.replaceFragment
import com.itmofitip.chesstimer.view.SettingsView
import com.itmofitip.chesstimer.view.TimeAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class SettingsFragment : Fragment(), SettingsView {

    private lateinit var presenter: SettingsPresenter
    private lateinit var timeRecyclerView: RecyclerView
    private lateinit var adapter: TimeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        view.findViewById<NestedScrollView>(R.id.scroll_view)?.fullScroll(ScrollView.FOCUS_UP)
        return view
    }

    override fun onStart() {
        super.onStart()
        presenter = SettingsPresenter(this)
        presenter.attach()
        initButtons()
        timeRecyclerView = requireActivity().findViewById(R.id.time_recycler_view)
        adapter = TimeAdapter(
            presenter.getTimeList(),
            presenter::onTimeItemChosen,
            this::onTimeItemLongClick
        )
        timeRecyclerView.adapter = adapter
        registerForContextMenu(timeRecyclerView)
    }

    override fun onStop() {
        super.onStop()
        presenter.detach()
    }

    override fun onTimeItemChosen() {
        APP_ACTIVITY.supportFragmentManager.popBackStack()
    }

    override fun setSwitchIsChecked(switchType: SwitchType, isChecked: Boolean) {
        with(requireActivity()) {
            when (switchType) {
                SwitchType.SOUND_ON_CLICK ->
                    findViewById<SwitchCompat>(R.id.switch_sound_on_click).isChecked = isChecked
                SwitchType.SOUND_ON_LOW_TIME ->
                    findViewById<SwitchCompat>(R.id.switch_sound_low_time).isChecked = isChecked
                SwitchType.VIBRATION ->
                    findViewById<SwitchCompat>(R.id.switch_vibration).isChecked = isChecked
                SwitchType.DARK_THEME ->
                    findViewById<SwitchCompat>(R.id.switch_dark_theme).isChecked = isChecked
            }
        }
    }

    private fun initButtons() {
        with(requireActivity()) {
            findViewById<ImageView>(R.id.return_button).setOnClickListener {
                APP_ACTIVITY.supportFragmentManager.popBackStack()
            }
            findViewById<SwitchCompat>(R.id.switch_sound_on_click).setOnCheckedChangeListener { _, isChecked ->
                presenter.onSwitchCheckedChange(SwitchType.SOUND_ON_CLICK, isChecked)
            }
            findViewById<SwitchCompat>(R.id.switch_sound_low_time).setOnCheckedChangeListener { _, isChecked ->
                presenter.onSwitchCheckedChange(SwitchType.SOUND_ON_LOW_TIME, isChecked)
            }
            findViewById<SwitchCompat>(R.id.switch_vibration).setOnCheckedChangeListener { _, isChecked ->
                presenter.onSwitchCheckedChange(SwitchType.VIBRATION, isChecked)
            }
            findViewById<SwitchCompat>(R.id.switch_dark_theme).setOnCheckedChangeListener { _, isChecked ->
                presenter.onSwitchCheckedChange(SwitchType.DARK_THEME, isChecked)
            }
            findViewById<TextView>(R.id.add_button).setOnClickListener {
                replaceFragment(AddTimeFragment(), true)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onTimeItemLongClick(position: Int, view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.inflate(R.menu.delete_menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_delete -> {
                    presenter.onDeleteInMenuSelected(position)
                    val newData = presenter.getTimeList()
                    adapter.setData(newData)
                    adapter.notifyDataSetChanged()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }
}