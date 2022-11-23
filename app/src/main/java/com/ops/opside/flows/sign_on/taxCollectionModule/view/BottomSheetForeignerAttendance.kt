package com.ops.opside.flows.sign_on.taxCollectionModule.view

import android.R
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputLayout
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
                val email = teEmail.text.toString()
                if (isValidEmail(email))
                    mViewModel.getEmailInformation(email)
                else
                    toast("Correo invalido")
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
            toast("Elige un tipo de registro")
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
                        email = teEmail.text.toString(),
                        name = teName.text.toString(),
                        origin = teOrigin.text.toString(),
                        linearMeters = teLinearMeters.text.toString().toDouble(),
                        role = if (cbConcessForeigner.isChecked) 1 else 2,
                        idFirebase = ID.getTemporalId(),

                        address = "",
                        phone = "",
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
                textField.editText?.text.toString() == "Municipio recidencia"
            ) {
                textField.error = getString(com.ops.opside.R.string.login_til_required)
                isValid = false
            } else {
                textField.error = null
            }
        }
        return isValid
    }

    private fun wasRegistered(idFirebase: String) {
        toast("Concesionario Registrado")

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
                requireContext(), R.layout.simple_list_item_1,
                origins.map { it.originName }.toMutableList()
            )

        mBinding.teOrigin.setAdapter(adapter)
    }

    private fun getEmailInformation(concessionaire: ConcessionaireSE?) {
        showForm(true)

        if (concessionaire == null) {
            toast("El concesionario no existe\nLlena los demás campos solicitados")
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

            teName.setText("")
            teLinearMeters.setText("")

            cbConcess.isGone = show.not()
            cbConcessForeigner.isGone = show.not()
            cbConcess.isClickable = true
            cbConcessForeigner.isClickable = true
            cbConcess.isChecked = false
            cbConcessForeigner.isChecked = false

            if (show.not()) tilEmail.helperText = "Ingresa un correo para buscar al concesionario"

            divider.visibility = if (show) View.VISIBLE else View.INVISIBLE
        }
    }
}