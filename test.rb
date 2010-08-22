require 'rubygems'
require 'fastercsv'
require 'set'
require 'redis'
require 'ratings'

#
#       +-----+-----+-----+-----+-----+
#       | I_1 | I_2 | I_3 | I_4 | I_5 |
# +-----+-----+-----+-----+-----+-----|
# | U_1 |   1 |  -1 |  -1 |     |   1 |
# |-----+-----+-----+-----+-----+-----|
# | U_2 |  -1 |     |     |  -1 |  -1 |
# |-----+-----+-----+-----+-----+-----|
# | U_3 |     |   1 |  1  |     |   1 |
# |-----+-----+-----+-----+-----+-----|
# | U_4 |   1 |  -1 |  1  |   1 |     |
# +-----+-----+-----+-----+-----+-----+
#

ratings = Ratings.new

# User 1
ratings.add('U_1', 'I_1', Ratings::Like)
ratings.add('U_1', 'I_2', Ratings::Dislike)
ratings.add('U_1', 'I_3', Ratings::Dislike)
ratings.add('U_1', 'I_5', Ratings::Like)

# User 2 (haters gonna hate)
ratings.add('U_2', 'I_1', Ratings::Dislike)
ratings.add('U_2', 'I_4', Ratings::Dislike)
ratings.add('U_2', 'I_5', Ratings::Dislike)

# User 3
ratings.add('U_3', 'I_2', Ratings::Like)
ratings.add('U_3', 'I_3', Ratings::Like)
ratings.add('U_3', 'I_5', Ratings::Like)

# User 4
ratings.add('U_4', 'I_1', Ratings::Like)
ratings.add('U_4', 'I_2', Ratings::Dislike)
ratings.add('U_4', 'I_3', Ratings::Like)
ratings.add('U_4', 'I_4', Ratings::Like)

puts ratings.suggest('U_4', ['I_5']).inspect
puts ratings.suggest('U_3', ['I_1', 'I_4'], :neighborhood => ['U_2']).inspect

# So one of the things you want to do is to filter
#  the items under consideration and the users in the neighborhood
#  of the user under consideration. This will allow you to achieve
#  better results while at the same time reducing computation costs