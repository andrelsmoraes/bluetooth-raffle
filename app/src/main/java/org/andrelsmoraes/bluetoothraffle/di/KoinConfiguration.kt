package org.andrelsmoraes.bluetoothraffle.di

import org.andrelsmoraes.bluetoothraffle.data.repository.DeviceRepositoryImpl
import org.andrelsmoraes.bluetoothraffle.data.repository.PreferencesRepositoryImpl
import org.andrelsmoraes.bluetoothraffle.data.repository.RaffledRepositoryImpl
import org.andrelsmoraes.bluetoothraffle.data.repository.api.BluetoothDeviceRepositoryImpl
import org.andrelsmoraes.bluetoothraffle.domain.interactor.GetRaffledDevicesUseCase
import org.andrelsmoraes.bluetoothraffle.domain.interactor.GetRemainingDevicesUseCase
import org.andrelsmoraes.bluetoothraffle.domain.interactor.RaffleDeviceUseCase
import org.andrelsmoraes.bluetoothraffle.domain.interactor.SearchDeviceUseCase
import org.andrelsmoraes.bluetoothraffle.domain.mapper.BluetoothDeviceMapper
import org.andrelsmoraes.bluetoothraffle.domain.repository.DeviceRepository
import org.andrelsmoraes.bluetoothraffle.domain.repository.PreferencesRepository
import org.andrelsmoraes.bluetoothraffle.domain.repository.RaffledRepository
import org.andrelsmoraes.bluetoothraffle.domain.repository.api.BluetoothDeviceRepository
import org.andrelsmoraes.bluetoothraffle.ui.DevicesViewModel
import org.andrelsmoraes.bluetoothraffle.ui.RaffledViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<DeviceRepository> { DeviceRepositoryImpl() }

    single<RaffledRepository> { RaffledRepositoryImpl() }

    single<PreferencesRepository> { PreferencesRepositoryImpl(get()) }

    single<BluetoothDeviceRepository> { BluetoothDeviceRepositoryImpl(get()) }

    single { BluetoothDeviceMapper() }

    factory { GetRemainingDevicesUseCase(get(), get()) }

    factory { SearchDeviceUseCase(get(), get(), get(), get()) }

    factory { RaffleDeviceUseCase(get(), get(), get()) }

    factory { GetRaffledDevicesUseCase(get()) }

    viewModel { DevicesViewModel(get(), get(), get()) }

    viewModel { RaffledViewModel(get()) }

}
