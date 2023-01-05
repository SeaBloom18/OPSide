package com.ops.opside.flows.sign_on.taxCollectionModule.view

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isGone
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputLayout
import com.ops.opside.R
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import com.ops.opside.common.entities.firestore.OriginFE
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.utils.ID
import com.ops.opside.common.utils.animateOnPress
import com.ops.opside.common.views.BaseBottomSheetFragment
import com.ops.opside.databinding.BottomSheetForeignerAttendanceBinding
import com.ops.opside.flows.sign_off.registrationModule.viewModel.RegisterViewModel
import com.ops.opside.flows.sign_on.taxCollectionModule.viewModel.BottomSheetForeignerAttendanceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetForeignerAttendance(
    private val registration: (ConcessionaireFE) -> Unit = {}
) : BaseBottomSheetFragment() {

    private val mBinding: BottomSheetForeignerAttendanceBinding by lazy {
        BottomSheetForeignerAttendanceBinding.inflate(layoutInflater)
    }

    private val mViewModel: BottomSheetForeignerAttendanceViewModel by viewModels()
    private val mRegistrationViewModel: RegisterViewModel by viewModels()

    private var mForeignerConcessionaire = ConcessionaireFE()
    private var existConcessionaire = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.apply {
            btnClose.animateOnPress()
            btnClose.setOnClickListener { dismiss() }

            tilEmail.setEndIconOnClickListener {
                val email = teEmail.text.toString().trim()
                if (isValidEmail(email)) mViewModel.getEmailInformation(email)
                else toast(getString(R.string.bottom_sheet_toast_invalid_email))
            }

            teEmail.doAfterTextChanged {
                showForm(false)
                mForeignerConcessionaire.idFirebase = ""
            }

            cbConcess.setOnClickListener { cbConcessForeigner.isChecked = false }
            cbConcessForeigner.setOnClickListener { cbConcess.isChecked = false }

            btnRegister.setOnClickListener { completeRegister() }
        }

        bindViewModel()
        mRegistrationViewModel.getOriginList()
    }

    private fun bindViewModel() {
        mViewModel.getEmailInformation.observe(this, Observer(this::getEmailInformation))
        mRegistrationViewModel.getOriginList.observe(this, Observer(this::getOriginList))
        mViewModel.registerConcessionaire.observe(this, Observer(this::wasRegistered))
    }

    private fun validateTypeOfRegister(): Boolean {
        if ((mBinding.cbConcess.isChecked.not() && mBinding.cbConcessForeigner.isChecked.not())
            && existConcessionaire.not()
        ) {
            toast(getString(R.string.bottom_sheet_toast_chose_register))
            return false
        }

        return true
    }

    private fun completeRegister() {
        if (validateFields(
                mBinding.tilEmail,
                mBinding.tilName,
                mBinding.tilOrigin,
                mBinding.tilLinearMeters
            ) &&
            isValidEmail(mBinding.teEmail.text.toString()) &&
            validateTypeOfRegister()
        ) {
            if (mForeignerConcessionaire.idFirebase.isNotEmpty()) {
                mForeignerConcessionaire.linearMeters =
                    mBinding.teLinearMeters.text.toString().toDouble()

                registration.invoke(mForeignerConcessionaire)

                dismiss()
            } else {
                with(mBinding) {
                    mForeignerConcessionaire = ConcessionaireFE(
                        isForeigner = cbConcessForeigner.isChecked,
                        email = teEmail.text.toString().trim(),
                        name = teName.text.toString().trim(),
                        origin = teOrigin.text.toString().trim(),
                        linearMeters = teLinearMeters.text.toString().toDouble(),
                        phone = tePhone.text.toString().trim(),
                        role = if (cbConcessForeigner.isChecked) 1 else 2,
                        idFirebase = ID.getTemporalId(),
                        address = "",
                        lineBusiness = "",
                        absence = 0,
                        password = "",
                        participatingMarkets = mutableListOf()
                    )
                }

                mViewModel.insertConcessionaire(mForeignerConcessionaire)
            }
        }
    }

    private fun validateFields(vararg textFields: TextInputLayout): Boolean {
        var isValid = true
        for (textField in textFields) {
            if (textField.editText?.text.toString().trim().isEmpty() ||
                textField.editText?.text.toString() == getString(R.string.bottom_sheet_tie_title)
            ) {
                textField.error = getString(R.string.login_til_required)
                isValid = false
            } else {
                textField.error = null
            }
        }
        return isValid
    }

    private fun wasRegistered(idFirebase: String) {
        toast(getString(R.string.bottom_sheet_toast_register_success))

        mForeignerConcessionaire.idFirebase = idFirebase
        registration.invoke(mForeignerConcessionaire)

        dismiss()
    }


    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun getOriginList(origins: MutableList<OriginFE>) {
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                requireContext(), android.R.layout.simple_list_item_1,
                origins.map { it.originName }.toMutableList()
            )

        mBinding.teOrigin.setAdapter(adapter)
    }

    private fun getEmailInformation(concessionaire: ConcessionaireSE?) {
        showForm(true)

        if (concessionaire == null) {
            toast(getString(R.string.bottom_sheet_toast_concessionaire_doesnt_exist))
            existConcessionaire = false
            return
        }

        setDataConcess(concessionaire)
        mForeignerConcessionaire = concessionaire.parseToFe()
    }

    private fun setDataConcess(concessionaire: ConcessionaireSE) {
        with(mBinding) {
            teName.setText(concessionaire.name)
            teOrigin.setText(concessionaire.origin)
            tePhone.setText(concessionaire.phone)

            tilLinearMeters.isGone = true
            teLinearMeters.setText("0.0")

            cbConcess.isChecked = concessionaire.isForeigner.not()
            cbConcessForeigner.isChecked = concessionaire.isForeigner

            cbConcess.isClickable = false
            cbConcessForeigner.isClickable = false

            existConcessionaire = true
        }
    }

    private fun showForm(show: Boolean) {
        with(mBinding) {
            tilName.isGone = show.not()
            tilOrigin.isGone = show.not()
            tilLinearMeters.isGone = show.not()
            tilEmail.isHelperTextEnabled = show.not()
            tilPhone.isGone = show.not()

            teName.setText("")
            teLinearMeters.setText("")
            tePhone.setText("")

            cbConcess.isGone = show.not()
            cbConcessForeigner.isGone = show.not()
            cbConcess.isClickable = true
            cbConcessForeigner.isClickable = true
            cbConcess.isChecked = false
            cbConcessForeigner.isChecked = false

            if (show.not()) tilEmail.helperText = getString(R.string.bottom_sheet_til_email_validation)

            divider.visibility = if (show) View.VISIBLE else View.INVISIBLE
        }
    }
}