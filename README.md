PresentPal
==========
# Fall 2012
## Caitlin Hurley

This repository contains PresentPal, an Android application that will enable users to keep track of gifts and their recipients. The user will be able to enter a recipient, then add/remove gifts as needed. They can also add in holidays so they know which holiday to buy each gift for. The user can then sort their list by recipient or by holiday. The app will display the number of gifts and total price for each recipient or holiday, depending on how it is sorted.

I would like to note that the code is not clean at all. The plan was to just write it and then clean it up but I don't have time to work on cleaning it up. Another thing to note is that there is no persistence. If the screen rotates, it starts over. That's especially important with the use of dialogs. And it's easiest to forget about after scanning a bar code. The barcode scanner works on a landscape view so either it needs to be rotated to portrait as soon as it is done scanning or kept at landscape. At some point, I would love to fix this but I don't have the time for this class's deliverables.
