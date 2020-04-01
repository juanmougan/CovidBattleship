package com.github.juanmougan.covidbattleship

// TODO add behaviour to change status here instead
enum class CellStatus(val imageId: Int) {
    SEA(R.drawable.blue_square_small) {
        override fun toggle() = SHIP
    }, SHIP(R.drawable.grey_square_small) {
        override fun toggle() = SEA
    };

    abstract fun toggle(): CellStatus

}
