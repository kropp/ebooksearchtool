from django.db import models

DICTIONARY_NAME_LENGTH = 255
WORD_LENGTH = 255
TRIGRAM_LENGTH = 3
TRIGRAM_FIELD_LENGTH = WORD_LENGTH * (TRIGRAM_LENGTH + 1)

class Dictionary(models.Model):
    name = models.CharField(max_length=DICTIONARY_NAME_LENGTH, unique=True)
    
    def correct(self):
        pass

    def __unicode__(self):
        return name


class Words(models.Model):
    word = models.CharField(max_length=WORD_LENGTH, unique=True)
    trigrams = models.CharField(max_length=TRIGRAM_FIELD_LENGTH)

    length = models.IntegerField()
    frequency = models.IntegerField()
    dictionary = models.ForeignKey(Dictionary)

    def __unicode__(self):
        string = "'%s' [freq %s] [dict %s]" % \
            self.word, self.length, self.dictionary.name
        return string


#Dictionary.get(name='title').correct('liight')
